package org.xhanka.ndm_project.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.xhanka.ndm_project.data.dao.EmergencyContactDao
import org.xhanka.ndm_project.data.dao.UserProfileDao
import org.xhanka.ndm_project.data.dao.UserLastKnownLocationDao
import org.xhanka.ndm_project.data.models.EmergencyContact
import org.xhanka.ndm_project.data.models.User
import org.xhanka.ndm_project.data.models.UserLastLKnownLocation

@Database(entities = [EmergencyContact::class, UserLastLKnownLocation::class, User::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class MainDataBase : RoomDatabase() {

    abstract fun emergencyContactNumbersDao(): EmergencyContactDao
    abstract fun userLastKnownLocationDao(): UserLastKnownLocationDao
    abstract fun userProfileDao(): UserProfileDao
}