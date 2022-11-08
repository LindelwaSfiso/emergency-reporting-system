package org.xhanka.ndm_project.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import org.xhanka.ndm_project.data.models.reporting.DashBoardMessage
import org.xhanka.ndm_project.data.models.reporting.EmergencyMessage
import org.xhanka.ndm_project.utils.Constants
import org.xhanka.ndm_project.utils.Constants.DB_EMERGENCIES_COLLECTION
import org.xhanka.ndm_project.utils.Constants.DB_USER_DASHBOARD_COLLECTION
import org.xhanka.ndm_project.utils.Utils
import kotlin.collections.HashMap


class DashboardViewModel : ViewModel() {

    private val db = Firebase.firestore

    private var _dashBoardMessages: MutableLiveData<List<DashBoardMessage>?> = MutableLiveData()
    val dashBoardMessages: LiveData<List<DashBoardMessage>?> = _dashBoardMessages

    private var _stateMessage: MutableLiveData<String> = MutableLiveData()
    val stateMessage: LiveData<String> = _stateMessage

    private var _conversationMessages: MutableLiveData<List<EmergencyMessage>> = MutableLiveData()
    val conversationMessages: LiveData<List<EmergencyMessage>> = _conversationMessages

    init {
        FirebaseFirestore.setLoggingEnabled(true)
    }

    fun subscribeToUserDashBoard(userId: String) {
        db.collection(DB_USER_DASHBOARD_COLLECTION).document(userId)
            .addSnapshotListener { querySnapshot, exception ->
                exception?.let {
                    _stateMessage.postValue(it.message)
                    return@addSnapshotListener
                }

                querySnapshot?.let {
                    // todo: refactor this code, restructure database if needs be
                    val list = mutableListOf<DashBoardMessage>()
                    val data = it.data?.values
                    data?.forEach { dashBoardMessage ->
                        // for each node, try to deserialize value
                        try {
                            val hash = dashBoardMessage as HashMap<*, *>
                            val message = DashBoardMessage(
                                lastMessage = hash["lastMessage"].toString(),
                                timeStamp = hash["timeStamp"].toString(),
                                uid = hash["uid"].toString(),
                                displayName = hash["displayName"].toString(),
                                senderUid = hash["senderUid"].toString()
                            )

                            try {
                                // try to deserialize TimeStamp to String Date
                                val timestamp = Regex(
                                    "[0-9]+",
                                    RegexOption.IGNORE_CASE
                                ).findAll(hash["timeStamp"].toString())
                                    .map(MatchResult::value).toList()
                                if (timestamp.size == 2) {
                                    message.timeStamp = Utils.formatDate(
                                        Timestamp(
                                            timestamp[0].toLong(), timestamp[1].toInt()
                                        ).toDate()
                                    )
                                }
                            } catch (ignore: Exception) {
                            }
                            list.add(message)
                        } catch (ignore: Exception) {
                            Log.d("TAG", ignore.message.toString())
                            ignore.printStackTrace()
                            _stateMessage.postValue(ignore.message.toString())
                        }
                    }
                    list.sortBy { a -> a.timeStamp }
                    _dashBoardMessages.postValue(list)
                }
            }

    }

    fun subscribeToUserConversation(conversationId: String) {
        Log.d("TAG", "SUBSCRIBING TO CONVERSATION")
        db.collection(DB_EMERGENCIES_COLLECTION).document(conversationId)
            .addSnapshotListener { querySnapshot, exception ->
                exception?.let {
                    _stateMessage.postValue(it.message)
                    return@addSnapshotListener
                }
                querySnapshot?.let {
                    val list = mutableListOf<EmergencyMessage>()
                    val data = it.data?.values
                    data?.forEach { conversationData ->
                        // for each node, try to deserialize value
                        try {
                            val messageData = conversationData as ArrayList<*>
                            messageData.forEach { conversationMessage ->
                                val hash = conversationMessage as HashMap<*, *>
                                val message = EmergencyMessage(
                                    emergencyMessageBody = hash["emergencyMessageBody"].toString(),
                                    timeStamp = hash["timeStamp"].toString(),
                                    emergencyLocation = hash["emergencyLocation"].toString(),
                                    emergencyType = hash["emergencyType"].toString(),
                                    senderUid = hash["senderUid"].toString()
                                )
                                try {
                                // try to deserialize TimeStamp to String Date
                                val timestamp = Regex("[0-9]+", RegexOption.IGNORE_CASE).findAll(hash["timeStamp"].toString())
                                    .map(MatchResult::value).toList()
                                if (timestamp.size == 2) {
                                    message.timeStamp = Utils.formatDate(
                                        Timestamp(timestamp[0].toLong(), timestamp[1].toInt()
                                        ).toDate()
                                    )
                                }
                            }catch (ignore: Exception) {}
                                list.add(message)
                            }
                        } catch (ignore: Exception) {
                            Log.d("TAG", "ERROR: conversation\t" + ignore.message.toString())
                            _stateMessage.postValue(ignore.message.toString())
                        }
                    }
                    list.sortBy { a -> a.timeStamp }
                    _conversationMessages.postValue(list)
                }
            }
    }

    /*fun sendFollowUpMessage(
        conversationId: String,
        response: String,
    ) {
        // update dashboard for both users
        // update message collection, append new message
        db.collection(DB_USER_DASHBOARD_COLLECTION)
            .document(userId)
            .update(DashBoardMessage(
                lastMessage = emergencyMessageBody,
                uid = emergencyMessageBody,
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
            .update("messages", EmergencyMessage(
                emergencyLocation = "${coordinates.latitude},${coordinates.longitude}",
                emergencyMessageBody = emergencyMessageBody,
                emergencyType = emergencyMessageBody
            ).toHash()).await()
    }*/
}
