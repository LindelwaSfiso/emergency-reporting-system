package org.xhanka.ndm_project

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.xhanka.ndm_project.databinding.ActivityMainBinding
import org.xhanka.ndm_project.ui.contacts.first_responder.EmergencyStationViewModel
import org.xhanka.ndm_project.ui.home.HomeViewModel
import org.xhanka.ndm_project.utils.Constants.REQUEST_LOCATION_PERMISSION_CODE
import org.xhanka.ndm_project.utils.Utils

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val homeViewModel by viewModels<HomeViewModel>()
    private val viewModel by viewModels<EmergencyStationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // add support toolbar, this is required to link with navController
        setSupportActionBar(binding.toolbar)
        val navView: BottomNavigationView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard_viewpager,
                R.id.navigation_dashboard, R.id.navigation_contacts,
                R.id.navigation_settings_screen
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home, R.id.navigation_dashboard_viewpager -> {
                    // show weather icon & temperature for home and dashboard fragments
                    binding.weatherTemperature.visibility = View.VISIBLE
                    binding.weatherIcon.visibility = View.VISIBLE
                }
                else -> {
                    // hide weather icon & temperature for other fragments
                    binding.weatherTemperature.visibility = View.GONE
                    binding.weatherIcon.visibility = View.GONE
                }
            }
            when(destination.id) {
                R.id.navigation_emergency_chat, R.id.navigation_emergency_stations -> {
                    binding.navView.visibility = View.GONE
                }
                else -> {
                    binding.navView.visibility = View.VISIBLE
                }
            }
        }

        viewModel.errorState.observe(this) {
            it?.let { message ->
                if (message.isNotEmpty())
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
            viewModel.resetMessage()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        // handle back button for non-top level menu fragments
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_LOCATION_PERMISSION_CODE && grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            // Permission denied.

            // Notify the user via a SnackBar that they have rejected a core permission for the
            // app, which makes the Activity useless. In a real app, core permissions would
            // typically be best requested during a welcome-screen flow.

            // Additionally, it is important to remember that a permission might have been
            // rejected without asking the user for permission (device policy or "Never ask
            // again" prompts). Therefore, a user interface affordance is typically implemented
            // when permissions are denied. Otherwise, your app could appear unresponsive to
            // touches or interactions which have required permissions.

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    REQUEST_LOCATION_PERMISSION_CODE
                )
            } else {

                AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_location)
                    .setTitle("Required Permission Denied")
                    .setMessage("This app needs access to your location to know where you are. Click OK to go to settings.")
                    .setPositiveButton("OK") { dialog: DialogInterface, _: Int ->
                        Utils.requestPermissionsFromSettings(this)
                        dialog.dismiss()
                    }.setOnCancelListener {
                        Log.d("TAG", "Dialog was cancelled ")
                    }
                    .show()
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        // setUpWeatherToolBar()
        return super.onCreateOptionsMenu(menu)
    }

    private fun setUpWeatherToolBar() {
        // make internet connection for weather and update user interface
        // if that fails don't display anything, try again later
        // menu?.get(0)?.title = "20\u2121"

        binding.weatherIcon.setOnClickListener {
            navController.navigate(R.id.navigation_weather)
        }

        binding.weatherTemperature.setOnClickListener {
            navController.navigate(R.id.navigation_weather)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.viewInMaps){
            homeViewModel.currentLocation.value?.let {
                startActivity(Intent(ACTION_VIEW, Uri.parse(
                    "geo:0,0?q=" + it.latitude +"," + it.longitude))
                )
            }
            return true
        } else if (item.itemId == R.id.manuallySyncDatabase) {
            AlertDialog.Builder(this)
                .setTitle("Sync")
                .setMessage("Manually sync first responder database?")
                .setPositiveButton("Sync") { _, _->
                    viewModel.retrieveFirstResponderDatabase()
                }.show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}