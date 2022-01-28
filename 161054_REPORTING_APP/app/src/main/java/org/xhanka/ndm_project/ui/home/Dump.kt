package org.xhanka.ndm_project.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.annotations.AfterPermissionGranted
import com.vmadalin.easypermissions.dialogs.DEFAULT_SETTINGS_REQ_CODE
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import org.xhanka.ndm_project.R
import java.text.DateFormat
import java.util.*

class Dump {
    //        binding.launchMap.setOnClickListener {
//            Toast.makeText(it.context, "geo:${locationGlobal?.latitude}, ${locationGlobal?.longitude}", Toast.LENGTH_SHORT).show()
//            val intent = Intent(
//                Intent.ACTION_VIEW, Uri.parse
//                    ("geo:46.7170627,-71.2884537")
//
//            )
//            startActivity(intent)
//        }

//    @SuppressLint("MissingPermission")
//    @AfterPermissionGranted(HomeFragment.REQUEST_LOCATION_PERMISSION_CODE)
//    fun getLastKnownLocation(activity: Activity) {
//
//        if (hasLocationPermissions()) {
//
//            // try to get previously saved location
//            // else tell user that there was not saved location found
//            homeViewModel.userLastLKnownLocation.value?.let {
//                binding.userLatitude.text = it.lastUpdateTime
//            } ?: run {
//                binding.userLatitude.text = "Last User Location Unknown"
//            }
//
//            fusedLocationClient.lastLocation.addOnCompleteListener(activity) { task ->
//                if (task.isSuccessful && task.result != null) {
//
//                    val location = task.result
//
//                    locationGlobal = location
//
////                homeViewModel.updateLocation(String.format(
////                    "%s\n%s",
////                    resources.getString(R.string.latitude_label, location.latitude),
////                    resources.getString(R.string.longitude_label, location.longitude)
////                ))
//
////
//
//                    Log.d(
//                        "TAG", resources
//                            .getString(R.string.latitude_label, task.result.latitude)
//                    )
//
//                    Log.d(
//                        "TAG", resources
//                            .getString(R.string.longitude_label, task.result.longitude)
//                    )
//
//                    homeViewModel.userLastLKnownLocation.observe(this, {
//                        it?.let { it1 ->
//                            // If not null, update location
//                            it1.lastUpdateTime = DateFormat.getTimeInstance().format(Date())
//                            it1.userLatitude = location.latitude
//                            it1.userLongitude = location.longitude
//
//
//
//                            Log.d("TAG", "SAVED LOCATION:\t$it")
//                        } ?: run {
//                            // if last known location is null, create a new entry and save location
//                            // this is likely to run when the app boot up for the first time
//
////                homeViewModel.sa
////
////                locationDao.saveLastKnownUserLocation(
////                    UserLastLKnownLocation(
////                        location.latitude,
////                        location.longitude,
////                        DateFormat.getTimeInstance().format(Date())
////                    ))
//                        }
//                    })
//
//                } else {
//                    task.exception?.printStackTrace()
//                    Log.d("TAG", "getLastLocation:exception")
//                }
//            }
//        } else {
//            EasyPermissions.requestPermissions(
//                this, "This app needs access to your location to know where you are.",
//                HomeFragment.REQUEST_LOCATION_PERMISSION_CODE,
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            )
//        }
//    }

//    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
//        Log.d("TAG", "PERMISSION DENIED")
//
//        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
//        // This will display a dialog directing them to enable the permission in app settings.
//        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
//            SettingsDialog.Builder(requireContext()).build().show()
//        }
//    }
//
//    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
//        Log.d("TAG", "PERMISSION GRANTED")
//    }
//
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
//    }
//
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == DEFAULT_SETTINGS_REQ_CODE) {
//            // Do something after user returned from app settings screen
//            Toast.makeText(requireContext(),
//                "${hasLocationPermissions()} : DOE IT HAVE PERMISSIONS",
//                Toast.LENGTH_LONG
//            ).show()
//        }
//    }

}