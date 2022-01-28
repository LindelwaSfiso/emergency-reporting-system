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
