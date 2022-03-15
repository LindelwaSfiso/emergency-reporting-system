package org.xhanka.ndm_project.data.utils

import android.view.View
import com.google.android.material.progressindicator.CircularProgressIndicator


/**
 * Utility class for handling network loading,
 * if the program is loading, a circular progress bar is shown
 * # Note: this will be later replaced by a NetworkBound class, which will load from
 * a cache database while getting data from internet
 */
class HandleNetworkLoading(
    private val progressBar: CircularProgressIndicator,
    private val contentContainer: View,
    private val statusContainer: View
) {
    fun hideLoading() {
        progressBar.visibility = View.GONE
        contentContainer.visibility = View.VISIBLE
        statusContainer.visibility = View.GONE
    }

    fun showError() {
        progressBar.visibility = View.GONE
        contentContainer.visibility = View.GONE
        statusContainer.visibility = View.VISIBLE
    }

    fun showLoading() {
        progressBar.visibility = View.VISIBLE
        contentContainer.visibility = View.GONE
        statusContainer.visibility = View.GONE
    }

}