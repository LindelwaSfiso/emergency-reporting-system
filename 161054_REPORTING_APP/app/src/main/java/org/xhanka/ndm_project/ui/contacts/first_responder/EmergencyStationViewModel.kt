package org.xhanka.ndm_project.ui.contacts.first_responder

import androidx.lifecycle.*
import androidx.room.withTransaction
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.xhanka.ndm_project.data.database.MainDataBase
import org.xhanka.ndm_project.data.models.contacts.EmergencyStation
import org.xhanka.ndm_project.utils.Constants.DB_STATIONS_COLLECTION
import javax.inject.Inject

@HiltViewModel
class EmergencyStationViewModel @Inject constructor(private val database: MainDataBase): ViewModel() {
    private val emergencyStationDao = database.emergencyStationsDao()

    val emergencyStations: LiveData<List<EmergencyStation>> =
        emergencyStationDao.getAllEmergencyStations()

    private var _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private var _errorState: MutableLiveData<String?> = MutableLiveData()
    val errorState: LiveData<String?> = _errorState

    fun retrieveFirstResponderDatabase() = CoroutineScope(Dispatchers.IO).launch {
        _loading.postValue(true)
        val db = Firebase.firestore
        try {
            val loadStations = db.collection(DB_STATIONS_COLLECTION).get().await()
            val stations = loadStations.toObjects<EmergencyStation>()
            syncEmergencyStationDatabase(stations)
            _errorState.postValue("Successfully updated first responder database.")
        } catch (ignore: Exception) { _errorState.postValue(ignore.message.toString()) }
        _loading.postValue(false)
    }

    private fun syncEmergencyStationDatabase(emergencyStations: List<EmergencyStation>) = viewModelScope.launch {
        database.withTransaction {
            emergencyStationDao.deleteAllEmergencyStations()
            emergencyStationDao.insertEmergencyStations(emergencyStations)
        }
    }

    fun resetMessage() {
        _errorState.postValue(null)
    }
}

