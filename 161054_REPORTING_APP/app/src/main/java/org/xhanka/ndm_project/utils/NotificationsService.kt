package org.xhanka.ndm_project.utils

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.lang.Exception

class NotificationsService : FirebaseMessagingService() {
    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        Log.d("TAG", p0.data.toString())

        Log.d("TAG", p0.notification?.body.toString())
    }

    override fun onMessageSent(p0: String) {
        super.onMessageSent(p0)
        Log.d("TAG", p0)
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

        Log.d("TAG", p0)
    }

    override fun onSendError(p0: String, p1: Exception) {
        super.onSendError(p0, p1)

        Log.d("TAG", p0)
    }
}