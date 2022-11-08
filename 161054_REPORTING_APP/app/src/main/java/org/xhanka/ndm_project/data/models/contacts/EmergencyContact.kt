package org.xhanka.ndm_project.data.models.contacts

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "EMERGENCY_CONTACTS")
data class EmergencyContact(
    var contactFullName: String,
    var contactPhoneNumber: String,

    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable