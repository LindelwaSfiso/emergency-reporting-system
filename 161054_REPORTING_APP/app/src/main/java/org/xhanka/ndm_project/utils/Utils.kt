package org.xhanka.ndm_project.utils

import android.app.*
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.telephony.SmsManager
import android.util.Log
import android.view.WindowManager
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import org.xhanka.ndm_project.BuildConfig
import org.xhanka.ndm_project.MainActivity
import org.xhanka.ndm_project.R
import org.xhanka.ndm_project.data.models.User
import org.xhanka.ndm_project.ui.dashboard.DashboardFragment
import org.xhanka.ndm_project.ui.report_emergency.ReportActivity
import org.xhanka.ndm_project.utils.Constants.CHANNEL_ID
import org.xhanka.ndm_project.utils.Constants.CHANNEL_NAME

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
        fun sendSmsToEmergencyContacts (
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

        
        // HANDLE DARK MODE FOR ANDROID 8 (Pie) and below devices
        fun setDarkMode(darkMode: String) {
            when (darkMode) {
                "0" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                "1" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                "2" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }

        private fun getDarkMode(context: Context?): String {
            context?.let {
                val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(it)
                return sharedPreferences.getString(Constants.DARK_MODE, "0").toString()
            } ?: run {
                return "0"
            }
        }

        fun toggleDarkMode(context: Context?) {
            context?.let {
                setDarkMode(getDarkMode(it))
            }
        }


        fun persistentNotification(context: Context) {
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
        }

    }
}