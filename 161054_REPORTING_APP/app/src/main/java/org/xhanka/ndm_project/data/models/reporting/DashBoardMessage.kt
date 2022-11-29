package org.xhanka.ndm_project.data.models.reporting

import android.os.Parcelable
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import org.xhanka.ndm_project.utils.Utils
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class DashBoardMessage(
    var uid: String = "",
    var lastMessage: String = "",
    var timeStamp: String = "",
    var displayName: String = "",
    val senderUid: String = ""
): Parcelable {
    fun toHash(conversationId: String): Map<String, Any> {
        return mapOf(
            "${conversationId}.lastMessage" to lastMessage,
            "${conversationId}.timeStamp" to FieldValue.serverTimestamp(),
            "${conversationId}.uid" to uid,
            "${conversationId}.senderUid" to senderUid,
            "${conversationId}.displayName" to displayName
        )
    }

    fun getConversationId(currentUid: String): String {
        return Utils.getGroupChatId(currentUid, uid)
    }
}

// todo: consider using serverTimestamp()