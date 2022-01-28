package org.xhanka.ndm_project.registration

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.xhanka.ndm_project.MainActivity
import org.xhanka.ndm_project.databinding.FragmentVerifyPhoneNumberBinding

class VerifyPhoneNumberFragment : Fragment() {
    
    private var _binding: FragmentVerifyPhoneNumberBinding ?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVerifyPhoneNumberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: HANDLE PHONE NUMBER VERIFICATION WITH FIREBASE

        binding.verifyPhoneNumber.setOnClickListener {
            startActivity(Intent(view.context, MainActivity::class.java))
            requireActivity().finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}