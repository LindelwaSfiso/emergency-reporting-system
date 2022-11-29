package org.xhanka.ndm_project.ui.registration.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import org.xhanka.ndm_project.MainActivity
import org.xhanka.ndm_project.databinding.FragmentLoginUserBinding
import org.xhanka.ndm_project.ui.registration.AuthViewModel

@AndroidEntryPoint
class LoginUserFragment : Fragment() {
    private var _binding: FragmentLoginUserBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logUserIn.setOnClickListener {
            signInWithEmailAndPassword()
        }

        binding.goToRegisterAccount.setOnClickListener { 
            val action = LoginUserFragmentDirections.goToRegisterAccount()
            findNavController().navigate(action)
        }
    }

    private fun signInWithEmailAndPassword() {
        val email = binding.userEmail.text.toString()
        val password = binding.userPassword.text.toString()

        // validate and sign in user
        if (!validateForm()) return

        toggleLoadingState()

        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener (requireActivity()) { task ->
                if (task.isSuccessful) {
                    // sign in user, redirect to home app
                    viewModel.getUserProfileAfterLogin(Firebase.auth.currentUser?.uid) {
                        it?.saveToPreferences(requireContext())
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                        requireActivity().finish()

                    }
                } else {
                    Snackbar.make(
                        requireView(),
                        task.exception?.message.toString(),
                        Snackbar.LENGTH_LONG
                    ).show()
                    toggleLoadingState(false)
                }
        }
    }

    private fun validateForm(): Boolean {
        val email = binding.userEmail.text.toString().trim()
        val password = binding.userPassword.text.toString().trim()

        var valid = true

        if (email.isEmpty()) {
            binding.userEmailContainer.error = "Email is Required."
            valid = false
        } else {
            binding.userEmailContainer.error = null
        }

        if (password.isEmpty()) {
            binding.userEmailContainer.error = "Enter valid password."
            valid = false
        } else {
            binding.userPasswordContainer.error = null
        }

        return valid
    }

    private fun toggleLoadingState(shouldShow: Boolean = true) {
        binding.progressBar.visibility = if(shouldShow) VISIBLE else INVISIBLE
        binding.userEmail.isEnabled = !shouldShow
        binding.userPassword.isEnabled = !shouldShow
        binding.logUserIn.isEnabled = !shouldShow
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}