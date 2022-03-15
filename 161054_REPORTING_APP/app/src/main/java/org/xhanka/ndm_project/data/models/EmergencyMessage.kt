package org.xhanka.ndm_project.data.models

import org.xhanka.ndm_project.utils.EmergencyType

data class EmergencyMessage(
    var location: String,
    var typeOfEmergency: EmergencyType,
    var localEmergencyStation: String,
    var user: User
) {
    fun toHash() {
        // todo
        val messageHash = HashMap<String, String>(4)
        messageHash["location"] = location
        messageHash["typeOfEmergency"] = typeOfEmergency.value
    }
}


// LOCATION : comma separated (19.19199, 199191)
// LOCAL_EMERGEnCY_StatION: Station which was chosen as the nearest service point

