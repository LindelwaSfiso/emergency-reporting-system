package org.xhanka.ndm_project.ui.settings

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import org.xhanka.ndm_project.R

class SettingsFragment : PreferenceFragmentCompat() {
    
    // TODO: SETTINGS FRAGMENT SLOW ON START UP --> INVESTIGATE, UPGRADE LIBRARIES

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        preference?.key?.let {
            if (it == "userProfile") {
                val action = SettingsFragmentDirections.actionNavigationSettingsToNavigationUserProfile()
                findNavController().navigate(action)
                return true
            }
        }

        return super.onPreferenceTreeClick(preference)
    }
}