package org.xhanka.ndm_project.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.xhanka.ndm_project.data.models.contacts.EmergencyContact

@Dao
interface EmergencyContactDao {

    @Query("SELECT * FROM EMERGENCY_CONTACTS")
    fun getAllEmergencyContacts(): LiveData<List<EmergencyContact>>

    @Query("DELETE FROM EMERGENCY_CONTACTS")
    suspend fun deleteAllEmergencyContacts()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmergencyContact(contact: EmergencyContact)

    @Update
    suspend fun updateEmergencyContact(contact: EmergencyContact)

    @Delete
    suspend fun deleteEmergencyContact(contact: EmergencyContact)
}