package org.xhanka.ndm_project.ui.settings

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import org.xhanka.ndm_project.R
import org.xhanka.ndm_project.utils.Constants
import org.xhanka.ndm_project.utils.Utils

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }
    
    // TODO: SETTINGS FRAGMENT SLOW ON START UP --> INVESTIGATE, UPGRADE LIBRARIES

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val darkMode: ListPreference? = findPreference(Constants.DARK_MODE)
        darkMode?.let { it ->
            it.setOnPreferenceChangeListener { _, newValue ->
                Utils.setDarkMode(newValue as String)
                true
            }
        }
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        preference.key.let {
            if (it == "userProfile") {
                val action = SettingsFragmentDirections.actionNavigationSettingsToNavigationUserProfile()
                findNavController().navigate(action)
                return true
            }
        }

        return super.onPreferenceTreeClick(preference)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
    }
}