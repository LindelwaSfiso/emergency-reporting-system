package org.xhanka.ndm_project.ui.contacts.personal

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.xhanka.ndm_project.data.database.MainDataBase
import org.xhanka.ndm_project.data.models.contacts.EmergencyContact
import javax.inject.Inject

@HiltViewModel
class EmergencyContactsViewModel @Inject constructor(private val database: MainDataBase): ViewModel() {
    private val emergencyContactDao = database.emergencyContactsDao()
    var emergencyContacts: LiveData<List<EmergencyContact>> = emergencyContactDao.getAllEmergencyContacts()

    fun addNewEmergencyContact(contact: EmergencyContact) = viewModelScope.launch {
        emergencyContactDao.insertEmergencyContact(contact)
    }

    fun updateEmergencyContact(contact: EmergencyContact) = viewModelScope.launch {
        emergencyContactDao.updateEmergencyContact(contact)
    }

    fun deleteEmergencyContact(contact: EmergencyContact) = viewModelScope.launch {
        emergencyContactDao.deleteEmergencyContact(contact)
    }

    fun sendEmergencySmsToContacts() {
        TODO("NOT YET IMPLEMENTED, FUNCTION WILL SEND SMS TO ALL CONTACTS")
    }
}