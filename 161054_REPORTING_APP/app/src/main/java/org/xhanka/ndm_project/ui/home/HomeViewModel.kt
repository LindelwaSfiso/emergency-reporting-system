package org.xhanka.ndm_project.ui.home

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.xhanka.ndm_project.data.database.MainDataBase
import org.xhanka.ndm_project.data.models.UserLastLKnownLocation
import java.text.DateFormat
import java.util.*
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(dataBase: MainDataBase): ViewModel() {

    private val locationDao = dataBase.userLastKnownLocationDao()
    private var _location: MutableLiveData<UserLastLKnownLocation?> = MutableLiveData()
    val userLastLKnownLocation: LiveData<UserLastLKnownLocation?> get() = _location

    // TODO: THIS IS A HACK :), CONSIDER A PERMANENT APPROACH
    private var _userDeniedPermissions: MutableLiveData<Boolean> = MutableLiveData(false)
    val userDeniedPermissions: LiveData<Boolean> get() = _userDeniedPermissions

    init {
        getLastKnownLocation()
    }

    fun updateUserLastKnownLocation(userLocation: UserLastLKnownLocation) = viewModelScope.launch {
        locationDao.updateUserLastKnownLocation(userLocation)
    }

    private fun getLastKnownLocation() = viewModelScope.launch {
        _location.postValue(locationDao.getLastKnownUserLocation())
        Log.d("TAG", _location.value.toString())
    }
    
    fun getL(location: Location) = viewModelScope.launch {
        locationDao.getLastKnownUserLocation()?.let {
            // If not null, update location
            it.lastUpdateTime = DateFormat.getTimeInstance().format(Date())
            it.userLatitude = location.latitude
            it.userLongitude = location.longitude

            locationDao.updateUserLastKnownLocation(it)

            Log.d("TAG", "SAVED LOCATION:\t$it")
        } ?: run {
            // if last known location is null, create a new entry and save location
            // this is likely to run when the app boot up for the first time
            locationDao.saveLastKnownUserLocation(
                UserLastLKnownLocation(
                    location.latitude,
                    location.longitude,
                    DateFormat.getTimeInstance().format(Date())
            ))
        }

    }


    // TODO: THIS IS A HACK :), CONSIDER A PERMANENT APPROACH
    // USED TO CHECK IF USER HAS PERMANENTLY DENIED PERMISSIONS
    // :) RARE CASE
    fun updateLocationPermissionStatus() {
        _userDeniedPermissions.postValue( true)
    }
}