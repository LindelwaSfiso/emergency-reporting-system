package org.xhanka.ndm_project.data.models

/**
 * Class modelling an emergency service point like a hospital, fire station or a police station
 */
data class EmergencyServicePoint (
    val stationName: String,
    val stationLatitude: Double,
    val stationLongitude: Double
)