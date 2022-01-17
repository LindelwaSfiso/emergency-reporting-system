package org.xhanka.ndm_project.ui.home
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import org.xhanka.ndm_project.R
import org.xhanka.ndm_project.databinding.FragmentHomeBinding
import org.xhanka.ndm_project.ui.activities.MapsActivity
import org.xhanka.ndm_project.utils.showSnackbar
import java.text.DateFormat
import java.util.*


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var mLocationCallback: LocationCallback? = null
    private var mLocationRequest: LocationRequest? = null


    companion object {
        private const val UPDATE_INTERVAL_IN_MILLI: Long = 5000
        private const val FASTEST_UPDATE_INTERVAL_IN_MILLI = UPDATE_INTERVAL_IN_MILLI / 2

    }

    private val homeViewModel by viewModels<HomeViewModel>()

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestPermissions()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity().application)

        createLocationRequest()
        createLocationCallback()

        fusedLocationClient.requestLocationUpdates(
            mLocationRequest!!,
            mLocationCallback!!,
            Looper.getMainLooper()
        )

        getLastKnownLocation(requireActivity())

        binding.launchMap.setOnClickListener {
            startActivity(Intent(requireContext(), MapsActivity::class.java))
        }
    }

    @SuppressLint("MissingPermission")
    fun getLastKnownLocation(activity: Activity) {
        fusedLocationClient.lastLocation.addOnCompleteListener(activity) { task ->
            if (task.isSuccessful && task.result != null) {

                val location = task.result

                homeViewModel.updateLocation(String.format(
                    "%s\n%s",
                    resources.getString(R.string.latitude_label, location.latitude),
                    resources.getString(R.string.longitude_label, location.longitude)
                ))

                val geocoder = Geocoder(requireContext())
                try {
                    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    val firstAddress = addresses[0]

                    binding.textPlace.text = String.format(
                        "%s\n%s\n%s\n%s",
                        firstAddress.getAddressLine(0),
                        firstAddress.countryName,
                        firstAddress.featureName,
                        firstAddress.adminArea
                    )
                }
                catch (exception: Exception) {
                    exception.printStackTrace()
                }

                Log.d(
                    "TAG", resources
                        .getString(R.string.latitude_label, task.result.latitude)
                )

                Log.d(
                    "TAG", resources
                        .getString(R.string.longitude_label, task.result.longitude)
                )

            } else {
                task.exception?.stackTrace
                Log.d("TAG", "getLastLocation:exception", task.exception)
            }
        }
    }


    private fun createLocationRequest() {
        mLocationRequest = LocationRequest.create()
        mLocationRequest!!.interval = UPDATE_INTERVAL_IN_MILLI
        mLocationRequest!!.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLI
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    /**
     * Creates a callback for receiving location events.
     */
    private fun createLocationCallback() {
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                val location = locationResult.lastLocation

                homeViewModel.updateLocation(String.format(
                    "%s\n%s",
                    resources.getString(R.string.latitude_label, location.latitude),
                    resources.getString(R.string.longitude_label, location.longitude)
                ))

                /*mCurrentLocation=*/
                Log.d("TAG", "Locations:\t" + locationResult.lastLocation)
                /*mLastUpdateTime =*/
                Log.d("LAST UPDATE TIME:\t", DateFormat.getTimeInstance().format(Date()))
            }
        }
    }

    private fun requestPermissions() {
        Log.d("TAG", "CHECKING PERMISSIONS")

        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    Log.i("Permission: ", "Granted")
                } else {
                    Log.i("Permission: ", "Denied")
                }
            }


        when {

            // IF PERMISSIONS ALREADY GIVEN PROCEED
            checkPermissions() -> {

                Log.d("TAG", "PERMISSION GRANTED")

                binding.snackBar.showSnackbar(
                    "GRANTED",
                    Snackbar.LENGTH_INDEFINITE,
                    null
                )
            }

            // REQUEST PERMISSIONS
            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                ACCESS_FINE_LOCATION
            ) -> {

                binding.snackBar.showSnackbar(
                    "Permission is required",
                    Snackbar.LENGTH_INDEFINITE, "Ok"
                ) {
                    requestPermissionLauncher.launch(
                        ACCESS_FINE_LOCATION
                    )
                }
            }

            else -> {
                requestPermissionLauncher.launch(
                    ACCESS_FINE_LOCATION
                )
            }
        }
    }

    private fun checkPermissions() =  ContextCompat.checkSelfPermission(
        requireContext(),
        ACCESS_FINE_LOCATION
    ) == PERMISSION_GRANTED

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onPause() {
        super.onPause()
        pauseLocationUpdates();
    }

    private fun pauseLocationUpdates() {
        mLocationCallback?.let { fusedLocationClient.removeLocationUpdates(it) }
    }

}

