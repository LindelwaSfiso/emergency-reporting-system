package org.xhanka.ndm_project

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import org.xhanka.ndm_project.utils.Utils

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		installSplashScreen()

		// toggle dark mode for android 8 and below, if enabled
		Utils.toggleDarkMode(this);

		super.onCreate(savedInstanceState)

		// -> TODO: SETUP DARK MODE HERE, IF ENABLED FOR ANDROID 8 and below

		startActivity(Intent(this, MainActivity::class.java))
		finish()

//		Firebase.auth.currentUser?.let {
//			// if not null, user has signed in --> redirect to mainActivity
//			startActivity(Intent(this, MainActivity::class.java))
//			finish()
//		} ?: run {
//			 // else redirect user to registrationActivity
//			startActivity(Intent(this, AuthActivity::class.java))
//			finish()
//		}

//		startActivity(Intent(this, MainActivity::class.java))
//		finish()
	}
}
