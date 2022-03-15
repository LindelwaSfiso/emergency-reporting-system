package org.xhanka.ndm_project.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import org.xhanka.ndm_project.data.models.User

@Dao
interface UserProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(user: User)

    @Update
    suspend fun updateUserProfile(user: User)
}