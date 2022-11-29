package org.xhanka.ndm_project.data.models.reporting

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import java.util.*

data class EmergencyMessage(
    var emergencyLocation: String = "",
    var emergencyType: String = "10",
    var emergencyMessageBody: String = "",
    var timeStamp: String = "",
    var senderUid: String = ""
) {

    companion object {
        fun toHash(
            emergencyLocation: String,
            emergencyType: String,
            emergencyMessageBody: String,
            senderUid: String
        ): FieldValue {
            return FieldValue.arrayUnion(
                mapOf(
                    "id" to UUID.randomUUID().toString(),
                    "emergencyLocation" to emergencyLocation,
                    "emergencyType" to emergencyType,
                    "emergencyMessageBody" to emergencyMessageBody,
                    "timeStamp" to Timestamp.now(),
                    "senderUid" to senderUid
                )
            )
        }
    }
}


// LOCATION : comma separated (19.19199, 199191)
// LOCAL_EMERGEnCY_StatION: Station which was chosen as the nearest service point

