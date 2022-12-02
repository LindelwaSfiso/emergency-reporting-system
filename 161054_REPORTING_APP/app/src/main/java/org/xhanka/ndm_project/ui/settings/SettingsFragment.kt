package org.xhanka.ndm_project.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import dagger.hilt.android.AndroidEntryPoint
import org.xhanka.ndm_project.R
import org.xhanka.ndm_project.ui.registration.AuthActivity
import org.xhanka.ndm_project.utils.Constants
import org.xhanka.ndm_project.utils.Utils

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat(), MenuProvider {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

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
            else if (it == "userLogout") {
                AlertDialog.Builder(requireContext())
                    .setTitle("Confirm")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Ok"){ _, _ ->
                        Firebase.auth.signOut()
                        Toast.makeText(requireContext(), "Successfully Signed Out!", Toast.LENGTH_LONG).show()
                        startActivity(Intent(requireContext(), AuthActivity::class.java))
                        requireActivity().finish()
                    }.show()
                return true
            }
        }

        return super.onPreferenceTreeClick(preference)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }

    companion object {
        const val DB_VERSION = "database_version"
    }
}