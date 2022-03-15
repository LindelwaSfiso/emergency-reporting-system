package org.xhanka.ndm_project.utils

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.provider.Settings
import android.telephony.SmsManager
import android.util.Log
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import org.xhanka.ndm_project.BuildConfig
import org.xhanka.ndm_project.data.models.User

class Utils {
    fun sendSmsToEmergencyContacts (@NonNull context: Context) {
        val smsManager = ContextCompat.getSystemService(context, SmsManager::class.java) as SmsManager
        smsManager.sendTextMessage(
           "+2676480479", null, "Hello", null, null
        )
    }


    companion object {
        fun requestPermissionsFromSettings(context: Context) {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts(
                "package",
                BuildConfig.APPLICATION_ID, null
            )
            intent.data = uri
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }


       fun getAddressFromLocation(context: Context, location: Location): Address? {
           val geocoder = Geocoder(context)
           return try {
               val addresses = geocoder.getFromLocation(
                   location.latitude, location.longitude, 1
               )
               addresses[0]
           } catch (exception: Exception) {
               exception.printStackTrace()
               null
           }
       }


        /**
         * Static function that validates the given number.
         *
         * -> we only support Eswatini Phone Numbers Only
         * -> A valid Eswatini Number has the following properties
         *      --- starts with 76, 78, 79
         *      --- is 8 characters long [without code]
         *
         *  Returns true if phone number is valid
         */
        fun isValidEswatiniPhoneNumber(areaCode: String = "+268", phoneNumber: String) : Boolean {
            if (phoneNumber.trim().length != 8 || areaCode != "+268")
                return false

            val firstTwoNumbers = phoneNumber.trim().subSequence(0, 2).toString()

            // 75 for testing purposes
            return firstTwoNumbers == "75" || firstTwoNumbers == "79" || firstTwoNumbers == "78" || firstTwoNumbers == "76"
        }


        /**
         * Static function for saving user profile to USERS' node in our database
         *
         * This happens after user has successfully logged into the app
         *  -- This will be used to set up one-to-one chats with local agents
         *
         *  Users DataBase structure
         *      -- userUid [unique user id]
         *          -- userFullName [user full name]
         *          -- userID [user identification number]
         *          -- userPhoneNumber [user phone number, with area code]
         *
         */
        fun createUserProfile(
            currentUser: FirebaseUser?,
            userFullName: String,
            userId: String,
            userPhoneNumber: String
        ) {
            currentUser?.let {
                val usersDataBase = FirebaseDatabase.getInstance().getReference("USERS")
                // save user to USERS_NODE

                // TODO: CHECK IF USER ALREADY EXISTS
                // IF EXITS -- OVERRIDE USER INFORMATION ELSE -- CREATE NEW NODE

                val user = User(
                    userFullName,
                    userId,
                    userPhoneNumber
                )

                val createUserTask = usersDataBase.child(it.uid).setValue(user.toHash())

                if (createUserTask.isSuccessful)
                    // todo: save user profile to local database
                    Log.d("TAG", "S")

            } ?: run {
                // for some reason this is null
            }
        }
    }
}