package org.xhanka.ndm_project.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.xhanka.ndm_project.R
import org.xhanka.ndm_project.databinding.FragmentRegisterUserBinding

class RegisterUserFragment: Fragment() {

    private var _binding: FragmentRegisterUserBinding ?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeAreCodes(view)

        // TODO: HANDLE USER REGISTRATION WITH FIREBASE

        binding.sendVerificationCode.setOnClickListener {
            val action = RegisterUserFragmentDirections.actionNavigationRegisterUserToNavigationVerifyPhoneNumber(
                "+26876480479"
            )
            findNavController().navigate(action)
        }
    }

    private fun initializeAreCodes(view: View) {
        // TODO: HANDLE OTHER AREA CODE, FOR NOW SHOWING ONLY FOR ESWATINI

        val adapter = ArrayAdapter(
            view.context,
            android.R.layout.simple_spinner_dropdown_item,
            requireActivity().resources.getStringArray(R.array.area_codes)
        )
        binding.autoComplete.setAdapter(adapter)

        // SINCE THERE IS ONLY ONE AREA CODE OPTION FOR NOW, JUST JUMP TO PHONE NUMBER FIELD
        binding.userPhoneNumber.requestFocus()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}