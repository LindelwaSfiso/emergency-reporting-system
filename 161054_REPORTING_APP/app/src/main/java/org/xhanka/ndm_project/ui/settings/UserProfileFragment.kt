package org.xhanka.ndm_project.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.xhanka.ndm_project.databinding.FragmentUserProfileBinding


/**
 * Fragment for modifying user information
 *
 * Information required from user:
 *  -> user ID
 *  -> user full name
 *  -> usr phone number [use registered phone number]
 *  - photo [!!!]
 *
 */

class UserProfileFragment : Fragment() {

    private var _binding: FragmentUserProfileBinding ?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}