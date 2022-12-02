package org.xhanka.ndm_project

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.preference.PreferenceManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.xhanka.ndm_project.ui.registration.AuthActivity
import org.xhanka.ndm_project.ui.settings.SettingsFragment.Companion.DB_VERSION
import org.xhanka.ndm_project.ui.settings.SettingsViewModel
import org.xhanka.ndm_project.utils.Utils

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
//	private val settingsViewModel by viewModels<SettingsViewModel> ()
//	private var isReady = false

	override fun onCreate(savedInstanceState: Bundle?) {
		installSplashScreen()
		// toggle dark mode for android 8 and below, if enabled
		Utils.toggleDarkMode(this)
		super.onCreate(savedInstanceState)

//		val manager = PreferenceManager.getDefaultSharedPreferences(this)
//		val version = manager.getString(DB_VERSION, "0.0").toString()

		Firebase.auth.currentUser?.let {
			// if not null, user has signed in --> redirect to mainActivity
			startActivity(Intent(this, MainActivity::class.java))
			finish()
		} ?: run {
			 // else redirect user to registrationActivity
			startActivity(Intent(this, AuthActivity::class.java))
			finish()
		}

	}
}
