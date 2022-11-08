package org.xhanka.ndm_project.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.xhanka.ndm_project.data.models.user.User

@Dao
interface UserProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(user: User)

    @Update
    suspend fun updateUserProfile(user: User)

    @Query("SELECT * FROM USER_PROFILE LIMIT 1;")
    fun getUserProfile(): LiveData<User>

    @Query("DELETE FROM USER_PROFILE")
    suspend fun removeUserProfile()
}