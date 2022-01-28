package org.xhanka.ndm_project.ui.home

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import org.xhanka.ndm_project.databinding.FragmentHomeBinding
import org.xhanka.ndm_project.utils.Constants.REQUEST_LOCATION_PERMISSION_CODE
import org.xhanka.ndm_project.utils.Utils
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var mLocationCallback: LocationCallback
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var mSettingsClient: SettingsClient
    private lateinit var mLocationSettingsRequest: LocationSettingsRequest


    private var mCurrentLocation: Location? = null
    private var userHasDeniedPermissions: Boolean = false

    companion object {
        // Update location every after 5 seconds :: Increase this value !!
        // NOTE: the app only request for location updates when HomeFragment is on foreground
        private const val UPDATE_INTERVAL_IN_MILLI: Long = 5000
        private const val FASTEST_UPDATE_INTERVAL_IN_MILLI = UPDATE_INTERVAL_IN_MILLI / 2
    }

    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireActivity().application)
        mSettingsClient = LocationServices.getSettingsClient(requireContext())

        createLocationRequest()
        createLocationCallback()
        buildLocationSettingsRequest()

        homeViewModel.userDeniedPermissions.observe(this, {
            Log.d("TAG", "COUNTING:\t$it")

                // TODO: THIS IS A HACK :), CONSIDER A PERMANENT APPROACH
                userHasDeniedPermissions = it

        })
    }


    private fun createLocationRequest() {
        mLocationRequest = LocationRequest.create()
        mLocationRequest.interval = UPDATE_INTERVAL_IN_MILLI
        mLocationRequest.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLI
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun buildLocationSettingsRequest() {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest)
            .setAlwaysShow(true)
        mLocationSettingsRequest = builder.build()
    }

    private fun createLocationCallback() {
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                mCurrentLocation = locationResult.lastLocation
                updateUI()
                // homeViewModel.getL(location)
                Log.d(
                    "TAG", "LATITUDE:\t${mCurrentLocation!!.latitude}, " +
                            "LONGITUDE:\t${mCurrentLocation!!.longitude}"
                )
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.

        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
            .addOnSuccessListener(requireActivity()) {
                Log.d("TAG", "All location settings are satisfied.")

                mFusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) {
                    Log.d("TAG", "GETTING LAST KNOWN LOCATION FORM FUSED LOCATION")

                    // todo: last known location has a very low accuracy > 2.5Km !!

                    if (it.isSuccessful && it.result != null) {
                        mCurrentLocation = it.result
                        updateUI()
                    }
                }

                // register for location updates
                mFusedLocationClient.requestLocationUpdates(
                    mLocationRequest,
                    mLocationCallback,
                    Looper.getMainLooper()
                )

            }
            .addOnFailureListener(requireActivity()) { e ->

                when ((e as ApiException).statusCode) {

                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        // either usr has not selected HIGH_PRECISION for location
                        // or their LOCATION is turned off

                        Log.d(
                            "TAG",
                            "Location settings are not satisfied. Attempting to upgrade " +
                                    "location settings "
                        )
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            val rae = e as ResolvableApiException
                            rae.startResolutionForResult(
                                requireActivity(),
                                REQUEST_LOCATION_PERMISSION_CODE
                            )
                        } catch (sie: SendIntentException) {
                            Log.d("TAG", "PendingIntent unable to execute request.")
                        }
                    }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {

                        val errorMessage = "Location settings are inadequate, and cannot be " +
                                "fixed here. Fix in Settings."

                        Log.d("TAG", errorMessage)
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
            }
    }

    private fun updateUI() {
        binding.userLatitude.text = mCurrentLocation?.latitude.toString()
        binding.userLongitude.text = mCurrentLocation?.longitude.toString()

        mCurrentLocation?.let {

            showLocationUpdates()

            binding.accuracy2.text = String.format("${it.accuracy} m")
            binding.lastUpdate2.text = SimpleDateFormat.getInstance().format(Date())
            binding.altitude2.text =
                if (it.hasAccuracy()) String.format("${it.altitude} m") else "N/A"
        } // consider showing errors if we can't get location
    }

    private fun requestPermissions() {
        Log.d("TAG", "CHECKING PERMISSIONS")

        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            ACCESS_FINE_LOCATION
        )

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.d("TAG", "Displaying permission rationale to provide additional context.")

            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION_CODE
            )

            return
        }

        if (userHasDeniedPermissions) {
            // user has permanently denied location permission, by checking "Don't ask again"
            // redirect user to settings so that they grant manually
            showPermissionError()
            binding.permissionDeniedContainer.requestPermissionsButton.setOnClickListener {
                Utils.requestPermissionsFromSettings(it.context)
            }

            return
        }

        // ELSE

        homeViewModel.updateLocationPermissionStatus()
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION),
            REQUEST_LOCATION_PERMISSION_CODE
        )
    }

    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            requireContext(),
            ACCESS_FINE_LOCATION
        )
        return permissionState == PERMISSION_GRANTED
    }

    override fun onResume() {
        super.onResume()

        if (mCurrentLocation == null)
        // remove this consider using view model to store current location
        // show loading animation while loading
            showInitializing()

        if (checkPermissions()) startLocationUpdates()
        else requestPermissions()
    }

    override fun onPause() {
        super.onPause()
        pauseLocationUpdates()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun pauseLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mLocationCallback.let { mFusedLocationClient.removeLocationUpdates(it) }
    }

    private fun showInitializing() {
        binding.displayContainer.visibility = View.GONE
        binding.locationInitializingContainer.locationInitializingContainer2.visibility =
            View.VISIBLE
        binding.permissionDeniedContainer.permissionDeniedContainer2.visibility = View.GONE
    }

    private fun showPermissionError() {
        binding.displayContainer.visibility = View.GONE
        binding.locationInitializingContainer.locationInitializingContainer2.visibility = View.GONE
        binding.permissionDeniedContainer.permissionDeniedContainer2.visibility = View.VISIBLE
    }

    private fun showLocationUpdates() {
        binding.displayContainer.visibility = View.VISIBLE
        binding.locationInitializingContainer.locationInitializingContainer2.visibility = View.GONE
        binding.permissionDeniedContainer.permissionDeniedContainer2.visibility = View.GONE
    }

}

