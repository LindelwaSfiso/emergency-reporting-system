package org.xhanka.ndm_project.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.xhanka.ndm_project.data.models.contacts.EmergencyStation

@Dao
interface EmergencyStationDao {
    @Query("SELECT * FROM FIRST_RESPONDER_DATABASE")
    fun getAllEmergencyStations(): LiveData<List<EmergencyStation>>

    @Query("DELETE FROM FIRST_RESPONDER_DATABASE")
    suspend fun deleteAllEmergencyStations()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmergencyStations(contact: List<EmergencyStation>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEmergencyStations(contact: EmergencyStation)
}