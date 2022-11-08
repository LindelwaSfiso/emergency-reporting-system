package org.xhanka.ndm_project.data.models.contacts

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Class modelling an emergency service point like a hospital, fire station or a police station
 */
@Entity(tableName = "FIRST_RESPONDER_DATABASE")
data class EmergencyStation (
    var stationCoordinates: String = "",
    var stationName: String = "",
    var stationTelephone: String = "",
    var stationType: Int = 10,
    var uid: String = "",

    @PrimaryKey(autoGenerate = true) val id: Int = 0
)


// 10 - MEDICAL
// 11 - POLICE
// 12 - FIRE