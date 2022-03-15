package org.xhanka.ndm_project.utils


/***
 * Type of emergency required by victim
 *
 * @author Dlamini Lindelwa Sifiso
 */

enum class EmergencyType(var value: String) {
    MEDICAL("MEDICAL"),
    FIRE( "FIRE"),
    POLICE("POLICE")
}