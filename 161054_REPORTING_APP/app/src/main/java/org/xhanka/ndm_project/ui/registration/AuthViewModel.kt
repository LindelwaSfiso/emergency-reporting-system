package org.xhanka.ndm_project.ui.registration

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.xhanka.ndm_project.data.database.MainDataBase
import org.xhanka.ndm_project.data.models.contacts.EmergencyStation
import org.xhanka.ndm_project.data.models.user.User
import org.xhanka.ndm_project.utils.Constants
import org.xhanka.ndm_project.utils.Constants.DB_USERS_COLLECTION
import org.xhanka.ndm_project.utils.Constants.DB_USER_DASHBOARD_COLLECTION
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(val database: MainDataBase): ViewModel() {
    private val userProfileDao = database.userProfileDao()
    private val emergencyStationDao = database.emergencyStationsDao()

    private val db = Firebase.firestore

    /**
     * Static function for saving user profile to USERS' node in our database
     *
     * This happens after user has successfully logged into the app
     *  -- This will be used to set up one-to-one chats with local agents
     *
     *  Users DataBase structure
     *      -- userUid [unique user id]
     *          -- userUid [id]
     *          -- userFullName [user full name]
     *          -- userID [user identification number]
     *          -- userPhoneNumber [user phone number, with area code]
     *
     */
    fun createUserProfile(
        currentUser: FirebaseUser?,
        userFullName: String,
        userEmail: String,
        userPhoneNumber: String,
        id: String,
        doneFunction: (user:User) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {

        currentUser?.let {
            val user = User(
                uid = currentUser.uid,
                fullName = userFullName,
                email = userEmail,
                ID = id,
                phoneNumber = userPhoneNumber
            )

            try {
                db.collection(DB_USERS_COLLECTION)
                    .document(currentUser.uid)
                    .set(user.toHash(), SetOptions.merge())
                    .await()    // add user to

                db.collection(DB_USER_DASHBOARD_COLLECTION)
                    .document(currentUser.uid)
                    .set(mapOf ("created" to "true"), SetOptions.merge())
                    .await()

                // if above operation is successful, save user locally
                userProfileDao.removeUserProfile()
                userProfileDao.insertUserProfile(user)
                syncDataBase()
                doneFunction (user)
            } catch (ignore: Exception) {
                Log.d("TAG", ignore.message.toString())
            }
        }
    }

    fun getUserProfileAfterLogin(uid: String?, doneFunction: (user: User?) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        var user: User ?= null
        try {
            uid?.let {
                val userProfile = db.collection(DB_USERS_COLLECTION).document(uid).get().await()
                user = userProfile.toObject<User>()
                user?.let {
                    userProfileDao.removeUserProfile()
                    userProfileDao.insertUserProfile(user!!)
                }
                syncDataBase()
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }

        // end activity if everything is done
        // consider retrying for getting profile
        doneFunction(user)
    }

    private fun syncDataBase() = viewModelScope.launch (Dispatchers.IO) {
        val db = Firebase.firestore
        try {
            val loadStations = db.collection(Constants.DB_STATIONS_COLLECTION).get().await()
            val stations = loadStations.toObjects<EmergencyStation>()
            database.withTransaction {
                emergencyStationDao.deleteAllEmergencyStations()
                emergencyStationDao.insertEmergencyStations(stations)
            }
            Log.d("TAG", "DONE UPDATING DATABASE")
        } catch (ignore: Exception) { }
    }
}