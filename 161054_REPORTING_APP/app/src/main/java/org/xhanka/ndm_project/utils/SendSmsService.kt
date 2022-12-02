package org.xhanka.ndm_project.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import org.xhanka.ndm_project.R
import org.xhanka.ndm_project.data.database.MainDataBase
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SendSmsService: LifecycleService() {
    private var fullName = ""
    private var location = ""

    @Inject
    lateinit var mainDataBase: MainDataBase
    private lateinit var notificationManager: NotificationManager

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if (intent != null && intent.action == ACTION_SEND_SMS) {
            fullName = intent.getStringExtra("fullName").toString()
            location = intent.getStringExtra("location").toString()
            startService()
            sendSms()
        } else stopService()
        return START_NOT_STICKY
    }


    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private fun sendSms() {
        val message = String.format(Locale.ENGLISH, DEFAULT_SMS_MESSAGE, fullName, location)
        val emergencyContactDao = mainDataBase.emergencyContactsDao()
        emergencyContactDao.getAllEmergencyContacts().observe(this@SendSmsService) {
            val totalCount = it.size
            Log.d("TAG", "STATIONS SIZE:\t$totalCount")
            it?.forEachIndexed { index, emergencyContact ->
                Log.d("TAG", "${emergencyContact.contactFullName} ${emergencyContact.contactPhoneNumber}")
                Utils.sendSmsToEmergencyContacts(
                    context = applicationContext,
                    toNumber = emergencyContact.contactPhoneNumber.trim(),
                    message = message
                )
                updateNotification(index, totalCount, createNotification())
            }
            val notify = createNotification()
            notify.setContentTitle("Sending complete.")
            notify.setContentText("Done sending sms --- $totalCount sent")
            notificationManager.notify(90, notify.build())
            stopService()
        }
    }

    private fun startService() {
        startForeground(NOTIFICATION_ID, createNotification().build())
    }

    private fun stopService() {
        stopForeground(false)
        stopSelf()
    }

    private fun updateNotification(
        progress: Int,
        totalMax: Int,
        notificationBuilder: NotificationCompat.Builder
    ) {
        notificationBuilder.setProgress(totalMax, progress, false)
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun createNotification(): NotificationCompat.Builder {
        // depending on the Android API that we're dealing with we will have
        // to use a specific method to create the notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Sending sms.",
                NotificationManager.IMPORTANCE_HIGH
            ).let {
                it.description = "Sending emergency sms to contacts."
                it.enableLights(true)
                it.lightColor = Color.RED
                it.enableVibration(false)
                it
            }
            notificationManager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setVibrate(null)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle("Sending emergency sms to contacts.")
            .setContentText("Sending....")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setSilent(true)
            .setTimeoutAfter(-1)
            .setAutoCancel(false)
    }

    companion object {
        const val NOTIFICATION_ID = 160
        const val NOTIFICATION_CHANNEL_ID = "com.xhanka.ndm"
        const val ACTION_SEND_SMS = "send_sms"
        const val DEFAULT_SMS_MESSAGE = "[ALERT] Hi? %s here. I'm in an emergency, you can find me here https://maps.google.com/?q=%s"
    }
}