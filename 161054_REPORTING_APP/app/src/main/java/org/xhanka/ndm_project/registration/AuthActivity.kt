package org.xhanka.ndm_project.registration

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import org.xhanka.ndm_project.R
import org.xhanka.ndm_project.databinding.ActivityAuthenticationBinding

/**
 * @author Dlamini Lindelwa [31/01/22]
 *
 * Authentication activity
 * +++++++++++++++++++++++
 *
 * Sub Fragments
 *      -- RegisterUserFragment.kt [get user phone number for verification]
 *      -- VerifyPhoneNumberFragment.kt [prompt user to enter verification code]
 *      --
 *
 * Logic Flow
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 *      -- prompt user to enter phone number
 *      -- verify number and send verification code
 *      -- prompt user to sign enter verification code
 *
 *      -- check if isNewUser
 *          -- if new
 *              -- prompt user to setup their profile
 *                  -- fullName, id, and phoneNumber
 *          -- if user already exits, log user in and redirect to MainActivity.kt
 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */
class AuthActivity: AppCompatActivity() {

    private var _binding: ActivityAuthenticationBinding ?= null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setSupportActionBar(binding.toolbar)

        navController = findNavController(R.id.nav_host_fragment_activity_authentication)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_register_user))
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}