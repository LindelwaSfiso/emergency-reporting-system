package org.xhanka.ndm_project

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		installSplashScreen()
		super.onCreate(savedInstanceState)

		// TODO: CHECK IF USER IS LOGGED IN,
		// -> IF NOT REDIRECT TO LOGIN SCREEN,
		// -> ELSE MOVE TO MAIN_ACTIVITY

		// -> TODO: SETUP DARK MODE HERE, IF ENABLED

		startActivity(Intent(this, MainActivity::class.java))
		finish()
	}
}
