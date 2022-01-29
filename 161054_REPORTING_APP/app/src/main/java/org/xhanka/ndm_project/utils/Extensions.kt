package org.xhanka.ndm_project.utils

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
    repeat(decimals) { multiplier *= 1.0}
    return kotlin.math.round(this * multiplier) / multiplier
}
