package org.xhanka.ndm_project.ui.report_emergency.process

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.xhanka.ndm_project.data.database.MainDataBase
import org.xhanka.ndm_project.data.models.reporting.DashBoardMessage
import org.xhanka.ndm_project.data.models.reporting.EmergencyMessage
import org.xhanka.ndm_project.utils.Constants
import org.xhanka.ndm_project.utils.Constants.DB_USER_DASHBOARD_COLLECTION
import org.xhanka.ndm_project.utils.Utils
import javax.inject.Inject

@HiltViewModel
class ProcessEmergencyViewModel @Inject constructor(dataBase: MainDataBase): ViewModel() {

    private val db = Firebase.firestore
    private val emergencyStationDao = dataBase.emergencyStationsDao()
    val user = dataBase.userProfileDao().getUserProfile()

    val allEmergencyStations = emergencyStationDao.getAllEmergencyStations()
    
    private var _done: MutableLiveData<Boolean> = MutableLiveData(false)
    val done: LiveData<Boolean> get() = _done

    /**
     * Function to send notification to emergency services
     */
    fun sendEmergencyNotificationToServices(
        userId: String,
        emergencyStationId: String,
        coordinates: Location,
        emergencyMessageBody: String,
        emergencyType: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        val conversationId = Utils.getGroupChatId(userId, emergencyStationId)

        // check if conversation between the two users exists
        val document = db.collection(Constants.DB_EMERGENCIES_COLLECTION)
            .document(conversationId).get().await()
        if (!document.exists())
            db.collection(Constants.DB_EMERGENCIES_COLLECTION)
                .document(conversationId).set(mapOf(
                    "created" to true,
                    "messages" to emptyList<String>() // investigate this
                ), SetOptions.merge()).await()

        // 1. set last message into [emergency_logs] under group_id/last_message
        // 2. insert new message into [emergency_logs] under group_id/messages

        // 1. Update dashboard of current user
        db.collection(DB_USER_DASHBOARD_COLLECTION)
            .document(userId)
            .update(DashBoardMessage(
                lastMessage = emergencyMessageBody,
                uid = emergencyStationId,
                senderUid = userId
            ).toHash(conversationId)).await()

        // 2. update dashboard of other user
        db.collection(DB_USER_DASHBOARD_COLLECTION)
            .document(emergencyStationId)
            .update(DashBoardMessage(
                lastMessage = emergencyMessageBody,
                uid = userId,
                senderUid = userId
            ).toHash(conversationId)).await()

        db.collection(Constants.DB_EMERGENCIES_COLLECTION)
            .document(conversationId)
            .update("messages", EmergencyMessage.toHash(
                emergencyLocation = "${coordinates.latitude},${coordinates.longitude}",
                emergencyMessageBody = emergencyMessageBody,
                emergencyType = emergencyType,
                senderUid = userId
            )).await()

        _done.postValue(true)

    }


}