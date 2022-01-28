package org.xhanka.ndm_project.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.provider.Settings
import android.telephony.SmsManager
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import org.xhanka.ndm_project.BuildConfig

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
    }
}