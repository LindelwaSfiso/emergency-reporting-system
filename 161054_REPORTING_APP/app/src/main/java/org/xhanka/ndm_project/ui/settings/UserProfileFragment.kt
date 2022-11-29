package org.xhanka.ndm_project.ui.settings

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.preference.PreferenceFragmentCompat
import dagger.hilt.android.AndroidEntryPoint
import org.xhanka.ndm_project.R


/**
 * Fragment for modifying user information
 *
 * Information required from user:
 *  -> user ID // user_id
 *  -> user full name // user_full_name
 *  -> usr phone number [use registered phone number] // user_phone_number
 */

@AndroidEntryPoint
class UserProfileFragment : PreferenceFragmentCompat(), MenuProvider {
    private val userViewModel by viewModels<UserViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.user_profile_preferences, rootKey)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }

    companion object {
        const val NAME_KEY = "user_full_name"
        const val PHONE_NUMBER_KEY = "user_phone_number"
        const val ID_KEY = "user_id"
        const val EMAIL_KEY = "user_email"
    }
}