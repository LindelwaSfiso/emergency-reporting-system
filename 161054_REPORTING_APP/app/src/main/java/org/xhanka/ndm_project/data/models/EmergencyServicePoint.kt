package org.xhanka.ndm_project.data.models

import org.xhanka.ndm_project.utils.EmergencyType

/**
 * Class modelling an emergency service point like a hospital, fire station or a police station
 */
data class EmergencyServicePoint (
    var stationName: String,
    var stationLatitude: Double,
    var stationLongitude: Double,
    var servicePhoneNumber: String,
    var serviceType: EmergencyType
)