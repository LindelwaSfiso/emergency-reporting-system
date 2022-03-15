package org.xhanka.ndm_project.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.encoders.annotations.Encodable

@IgnoreExtraProperties
@Entity(tableName = "USER_PROFILE")
data class User(
    var userFullName: String,
    var userID: String,
    var userPhoneNumber: String,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
) {

    @Encodable.Ignore
    fun toHash(): Map<String, String> {
        return mapOf(
            "FULL_NAME" to userFullName,
            "ID" to userID,
            "PHONE_NUMBER" to userPhoneNumber
        )
    }
}

// ESWATINI ID IS 13 DIGITS LONG