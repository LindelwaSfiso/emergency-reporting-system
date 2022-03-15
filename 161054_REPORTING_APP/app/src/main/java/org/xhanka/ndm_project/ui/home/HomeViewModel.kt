package org.xhanka.ndm_project.ui.home

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class HomeViewModel : ViewModel() {

    // save current location, avoid losing this when screen changes, or user goes to another screen
    private var _currentLocation: MutableLiveData<Location> = MutableLiveData()
    val currentLocation: LiveData<Location> get() = _currentLocation

    // TODO: THIS IS A HACK :), CONSIDER A PERMANENT APPROACH, (FIX TO GOOGLE'S BROKEN CODE!!)
    private var _userDeniedPermissions: MutableLiveData<Boolean> = MutableLiveData(false)
    val userDeniedPermissions: LiveData<Boolean> get() = _userDeniedPermissions

    // TODO: THIS IS A HACK :), CONSIDER A PERMANENT APPROACH
    // USED TO CHECK IF USER HAS PERMANENTLY DENIED PERMISSIONS
    // :) RARE CASE
    fun updateLocationPermissionStatus() {
        _userDeniedPermissions.postValue(true)
    }


    /**
     * Update the current user location,
     */
    fun setCurrentLocation(location: Location) {
        // update user interface with the newest location
        _currentLocation.postValue(location)


//        viewModelScope.launch {
//            // save last known location to database via coroutines
//            // --> the app tries to save the location with the best location accuracy
//            val savedLocation = currentLocationDao.getLastKnownUserLocation()
//            savedLocation?.let {
//                // location is available, try to compare accuracy and save the best
//                if (location.accuracy > it.locationAccuracy) {
//
//                    it.locationAccuracy = location.accuracy
//                    it.lastUpdateTime = SimpleDateFormat(
//                        "yy/MM -- HH:mm:ss", Locale.getDefault()
//                    ).format(Date())
//
//                    currentLocationDao.updateUserLastKnownLocation(it)
//                }
//            } ?: run {
//                // location is null (nothing saved yet??)
//                // -> update location
//
//                currentLocationDao.saveLastKnownUserLocation(
//                    UserLastLKnownLocation(
//                        location.latitude, location.longitude, SimpleDateFormat(
//                            "yy/MM -- HH:mm:ss", Locale.getDefault()
//                        ).format(Date()), location.accuracy
//                    )
//                )
//            }
//        }
    }

}