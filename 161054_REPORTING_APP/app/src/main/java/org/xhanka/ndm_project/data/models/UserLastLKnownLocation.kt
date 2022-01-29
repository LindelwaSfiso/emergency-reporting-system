package org.xhanka.ndm_project.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LAST_KNOWN_LOCATION")
data class UserLastLKnownLocation(
    var userLatitude: Double,
    var userLongitude: Double,

    // Last time we collected user location
    var lastUpdateTime: String,

    var locationAccuracy: Float,

    // todo: for advanced usage, consider tracking user, and saving locations after a certain
    // todo: elapsed period (e.g. ten minutes)


    @PrimaryKey(autoGenerate = true) val id: Int = 0
)