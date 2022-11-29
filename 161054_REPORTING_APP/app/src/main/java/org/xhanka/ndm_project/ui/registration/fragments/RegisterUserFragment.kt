package org.xhanka.ndm_project.ui.registration.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import org.xhanka.ndm_project.MainActivity
import org.xhanka.ndm_project.R
import org.xhanka.ndm_project.databinding.FragmentRegisterUserBinding
import org.xhanka.ndm_project.ui.registration.AuthViewModel
import org.xhanka.ndm_project.utils.Utils


@AndroidEntryPoint
class RegisterUserFragment : Fragment() {

    private var _binding: FragmentRegisterUserBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by viewModels<AuthViewModel>()
    private val auth: FirebaseAuth = Firebase.auth

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
        initializeAreaCodes(view)

        binding.registerAccount.setOnClickListener {
            val fullName = binding.userFullName.text.toString().trim()
            val phoneNUmber = binding.userPhoneNumber.text.toString().trim()
            val email = binding.userEmail.text.toString().trim()
            val password = binding.userPassword.text.toString().trim()
            val id = binding.userID.text.toString().trim()

            createAccountWithEmailAndPassword(email, password, fullName, phoneNUmber, id)
        }
    }

    private fun initializeAreaCodes(view: View) {
        val adapter = ArrayAdapter(
            view.context,
            android.R.layout.simple_spinner_dropdown_item,
            requireActivity().resources.getStringArray(R.array.area_codes)
        )
        binding.areaCode.setAdapter(adapter)
    }

    private fun validateForm(): Boolean {
        val email = binding.userEmail.text.toString().trim()
        val password = binding.userPassword.text.toString().trim()
        val fullName = binding.userFullName.text.toString().trim()
        val phoneNumber = binding.userPhoneNumber.text.toString().trim()
        val iD = binding.userID.text.toString().trim()

        var valid = true

        if (email.isEmpty()) {
            binding.userEmailContainer.error = "Email is Required."; valid = false
        } else {
            binding.userEmailContainer.error = null
        }

        if (fullName.isEmpty()) {
            binding.userFullNameContainer.error = "Enter valid password."
            valid = false
        } else {
            binding.userFullNameContainer.error = null
        }

        if (password.isEmpty()) {
            binding.userEmailContainer.error = "Enter valid password."
            valid = false
        } else {
            binding.userPasswordContainer.error = null
        }

        if (phoneNumber.isEmpty()) {
            binding.userPhoneNumberContainer.error = "Enter valid password."
            valid = false
        } else {
            binding.userPhoneNumberContainer.error = null
        }

        if (iD.isEmpty()) {
            binding.userIDContainer.error = "Enter valid ID."
            valid = false
        } else {
            binding.userIDContainer.error = null
        }

        return valid
    }

    private fun createAccountWithEmailAndPassword(
        email: String,
        password: String,
        fullName: String,
        phoneNumber: String,
        id: String
    ) {
        if (!validateForm()) return
        toggleLoadingState()

        Log.d("TAG", "BEGIN CREATE ACCOUNT")

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "CREATE SUCCESSFUL")
                    authViewModel.createUserProfile(
                        Firebase.auth.currentUser,
                        fullName,
                        email,
                        phoneNumber,
                        id
                    ) { user ->
                        user.saveToPreferences(requireContext())
                    }

                    Log.d("TAG", "CREATE DONE, REDIRECTING")
                    // redirect to home page, successfully logged in
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().finish()
                }
                else {
                    Snackbar.make(
                        requireView(),
                        task.exception?.message.toString(),
                        Snackbar.LENGTH_LONG
                    ).show()
                    toggleLoadingState(false)
                }
            }
    }

    private fun toggleLoadingState(shouldShow: Boolean = true) {
        binding.progressBar.visibility = if(shouldShow) View.VISIBLE else View.INVISIBLE
        binding.userFullName.isEnabled = !shouldShow
        binding.userEmail.isEnabled = !shouldShow
        binding.userPhoneNumber.isEnabled = !shouldShow
        binding.userPassword.isEnabled = !shouldShow
        binding.registerAccount.isEnabled = !shouldShow
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}