package org.xhanka.ndm_project.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.xhanka.ndm_project.data.models.UserLastLKnownLocation


@Dao
interface UserLastKnownLocationDao {

    @Query("SELECT * FROM LAST_KNOWN_LOCATION LIMIT 1")
    suspend fun getLastKnownUserLocation(): UserLastLKnownLocation?

    // save user last known location, onConflict, replace existing entry
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLastKnownUserLocation(userLocation: UserLastLKnownLocation)

    @Update
    suspend fun updateUserLastKnownLocation(userLocation: UserLastLKnownLocation)

    @Delete
    suspend fun deleteUserLastKnownLocation(userLocation: UserLastLKnownLocation)
}