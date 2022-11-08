package org.xhanka.ndm_project.utils

import android.speech.SpeechRecognizer.*
import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(
    msg: String,
    length: Int,
    actionMessage: CharSequence?,
    action: ((View) -> Unit)? = null
) {
    val snackbar = Snackbar.make(this, msg, length)
    if (actionMessage != null) {
        snackbar.setAction(actionMessage) {
            if (action != null) {
                action(this)
            }
        }.show()
    } else {
        snackbar.show()
    }
}


/***
 * Extension function to round Double to nearest :decimals
 */

fun Double.round(decimals: Int = 2): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 1.0 }
    return kotlin.math.round(this * multiplier) / multiplier
}

/**
 * Extension to create background color based on user initials
 */

fun String.avatarBackground(): String {
    val trimmed = this.trim()

    /*return trimmed.split("\\s+")
        .mapNotNull { it.firstOrNull()?.toString() }
        .reduce { acc, s -> acc + s }
        .uppercase()*/
    return trimmed.split("\\b[a-zA-Z]")
        .mapNotNull { it.firstOrNull()?.toString() }
        .reduce { acc, s -> acc + s }
        .uppercase()
}


fun Int.getErrorCode(): String {
    return when (this) {
        ERROR_NETWORK -> "Network error."
        ERROR_AUDIO -> "error audio"
        ERROR_CLIENT -> "error client"
        ERROR_INSUFFICIENT_PERMISSIONS -> "insufficient permissions"
        ERROR_LANGUAGE_NOT_SUPPORTED -> "error language not supported"
        ERROR_LANGUAGE_UNAVAILABLE -> "error language unavailable"
        ERROR_NETWORK_TIMEOUT -> "error timeout"
        ERROR_NO_MATCH -> "error no matches"
        ERROR_RECOGNIZER_BUSY -> "error recognizer busy"
        ERROR_SERVER -> "error server"
        ERROR_SERVER_DISCONNECTED -> "error server disconnected"
        ERROR_SPEECH_TIMEOUT -> "error speech timeout"
        ERROR_TOO_MANY_REQUESTS -> "error too many requests"
        else -> "i also don't know what happened :)"
    }
}

fun String.parseCoordinates(): Pair<Double, Double>{
    val splits = this.split(",")
    return Pair(splits[0].trim().toDouble(), splits[1].trim().toDouble())
}

