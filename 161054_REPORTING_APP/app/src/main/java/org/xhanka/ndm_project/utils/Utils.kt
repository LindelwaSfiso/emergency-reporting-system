package org.xhanka.ndm_project.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import org.xhanka.ndm_project.BuildConfig
import org.xhanka.ndm_project.ui.report_emergency.report.ReportEmergencyViewModel
import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object {
        /**
         * Function to redirect user to settings so that they grant required permissions
         */
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

        @Suppress("DEPRECATION")
        fun sendSmsToEmergencyContacts(
            @NonNull context: Context,
            toNumber: String,
            message: String
        ) {
            val smsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // works for API 31 and above
                ContextCompat.getSystemService(context, SmsManager::class.java) as SmsManager
            } else {
                // Note this only works if you have a single sim or you have set a default sim for
                // sending sms in a dual phone.
                SmsManager.getDefault()
            }
            smsManager.sendTextMessage(
                toNumber, null, message, null, null
            )
        }

        private fun checkCallPermission(activity: Activity, callback: (granted: Boolean) -> Unit) {
            callback(
                ContextCompat.checkSelfPermission(
                    activity, Manifest.permission.CALL_PHONE
                ) == PackageManager.PERMISSION_GRANTED
            )
        }

        fun launchCallIntent(
            recipient: String,
            activity: Activity?,
            onErrorCallBack: (error: String) -> Unit,
            endActivity: () -> Unit
        ) {
            activity?.let {
                checkCallPermission(activity) {
                    // check if user has granted permissions
                    if (it) {
                        val action = Intent.ACTION_CALL
                        Intent(action).apply {
                            data = Uri.fromParts("tel", recipient, null)
                            activity.startActivity(this)
                        }
                        // delay 200 milliseconds then end activity
                        Handler(Looper.getMainLooper()).postDelayed({
                            endActivity()
                        }, 500)
                    } else {
                        onErrorCallBack(
                            "App requires 'CALL' Permission to make call. " +
                                    "Grant required call permissions!"
                        )
                    }
                }
            } ?: run {
                onErrorCallBack("An error occurred! Try Again")
            }
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

        // HANDLE DARK MODE FOR ANDROID 8 (Pie) and below devices
        fun setDarkMode(darkMode: String) {
            when (darkMode) {
                "0" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                "1" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                "2" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }

        fun toggleDarkMode(context: Context?) {
            val darkMode = context?.let {
                val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(it)
                sharedPreferences.getString(Constants.DARK_MODE, "0").toString()
            } ?: run { "0" }

            setDarkMode(darkMode)
        }

        fun getGroupChatId(userOneId: String, userTwoId: String): String {
            return if (userOneId < userTwoId) "${userOneId}_{$userTwoId}"
            else "${userTwoId}_${userOneId}"
        }

        fun formatDate(date: Date): String {
            return try {
                SimpleDateFormat(
                    "yy/MM -- HH:mm:ss", Locale.getDefault()
                ).format(date)
            } catch (ignore: Exception) {
                date.toString()
            }
        }

        fun formatDate2(date: Date): String {
            return try {
                SimpleDateFormat(
                    "yy/MM", Locale.getDefault()
                ).format(date)
            } catch (ignore: Exception) {
                date.toString()
            }
        }

        private val MEDICAL =
            listOf("ambulance", "doctor", "hospital", "sick", "dead", "car accidents")
        private val FIRE = listOf("fire", "burning", "forest", "extinguish")
        private val POLICE = listOf("police", "crime", "hijacked", "hijacking")

        fun searchForKeyWords(translatedText: String): String {
            val score = arrayOf(0, 0, 0)
            // todo: Improve selection algorithm

            // test for medical keywords
            for (keyword in MEDICAL) {
                if (translatedText.contains(keyword, ignoreCase = true)) {
                    // found match, increase medical score
                    Log.d("TAG", "medical:  $keyword")
                    score[0]++
                }
            }

            // test for fire keywords
            for (keyword in FIRE) {
                if (translatedText.contains(keyword, ignoreCase = true)) {
                    // found match, increase medical score
                    Log.d("TAG", "fire:  $keyword")
                    score[1]++
                }
            }

            // test for fire keywords
            for (keyword in POLICE) {
                if (translatedText.contains(keyword, ignoreCase = true)) {
                    // found match, increase medical score
                    Log.d("TAG", "police:  $keyword")
                    score[2]++
                }
            }

            Log.d("TAG", "score: medical - ${score[0]}  fire - ${score[1]}  police - ${score[2]}")

            if (score[0]==0 && score[1]==0 && score[2]==0) return "NO MATCHES, TRY AGAIN"

            var indexValue = 0
            var max = 0
            score.forEachIndexed { index, i ->
                if (i >= max) {
                    max = i
                    indexValue = index
                }
            }
            return when (indexValue) {
                0 -> "MEDICAL"
                1 -> "FIRE"
                else -> "POLICE"
            }
        }

        /*fun persistentNotification(context: Context) {
            val notificationManager by lazy { NotificationManagerCompat.from(context) }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
            ).apply {
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }

            notificationManager.createNotificationChannel(channel)

            val contentIntent = Intent(context, ReportActivity::class.java)
            val contentPendingIntent = PendingIntent.getActivity(
                context, 0, contentIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val fullScreenIntent = Intent(context, ReportActivity::class.java)
            val fullScreenPendingIntent = PendingIntent.getActivity(
                context, 0, fullScreenIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_home_black)
                .setColor(ResourcesCompat.getColor(context.resources, R.color.purple_200, null))
                .setContentTitle("Heads Up Notification")
                .setAutoCancel(true)
                //.setContentIntent(contentPendingIntent)
                .setFullScreenIntent(fullScreenPendingIntent, true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .build()

            notificationManager.notify(1, notification)
        }

        fun showOnLockScreenAndTurnScreenOn(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                activity.setShowWhenLocked(true)
                activity.setTurnScreenOn(true)
            } else {
                activity.window.addFlags(
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                            or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                )
            }
        }*/

    }

    class VerticalSpace(private val verticalSpace: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.bottom = verticalSpace
        }
    }
}
