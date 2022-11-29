package org.xhanka.ndm_project.data.models.reporting

import com.google.firebase.Timestamp

/**
 * Class to represent a public notice message.
 *
 * @param message The message of the public notice.
 * @param timeStamp The time it was sent.
 * @param type Type of public notice, e.g. MEDICAL, FIRE, POLICE
 * @param read If message was read.
 */
data class PublicNotice (
    val message: String = "",
    val timeStamp: Timestamp = Timestamp.now(),
    val noticeType: String = "",
    val read: Boolean = false
)


/*
{
    timeStamp=Timestamp(seconds=1669123924, nanoseconds=104000000),
    noticeType=MEDICAL,
    message=There has been an accident on route MR-3, drive with caution.
}
*/
