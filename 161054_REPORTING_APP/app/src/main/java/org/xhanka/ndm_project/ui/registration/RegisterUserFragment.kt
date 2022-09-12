package org.xhanka.ndm_project.ui.registration

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.xhanka.ndm_project.MainActivity
import org.xhanka.ndm_project.R
import org.xhanka.ndm_project.databinding.FragmentRegisterUserBinding
import org.xhanka.ndm_project.utils.Utils
import java.util.concurrent.TimeUnit


// TEST NUMBER: +268 7548 0479
// VERIFICATION CODE: 161054

class RegisterUserFragment : Fragment() {

    private var _binding: FragmentRegisterUserBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private var verificationInProgress = false
    private var storedVerificationId: String? = ""
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks


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

        initializeFireBaseAuth(view)

        binding.sendVerificationCode.setOnClickListener {

            if (!validateUserInformation()) {
                return@setOnClickListener
            }

            startPhoneNumberVerification(getUserPhoneNumber())
        }
    }

    private fun validateUserInformation(): Boolean {
        val numberIsValid = Utils.isValidEswatiniPhoneNumber(
            binding.areaCode.text.toString().trim(),
            binding.userPhoneNumber.text.toString().trim()
        )

        val nameValid = !TextUtils.isEmpty(binding.userFullName.text.toString())
        val idValid = TextUtils.isDigitsOnly(binding.userID.text.toString())
                && binding.userID.text.toString().length == 13

        if (!numberIsValid) binding.userPhoneNumberContainer.error = "Not valid"
        if (!nameValid) binding.userFullNameContainer.error = "Not valid"
        if (!idValid) binding.userIDContainer.error = "Not valid"

        return numberIsValid && nameValid && idValid
    }

    private fun initializeAreaCodes(view: View) {
        val adapter = ArrayAdapter(
            view.context,
            android.R.layout.simple_spinner_dropdown_item,
            requireActivity().resources.getStringArray(R.array.area_codes)
        )
        binding.areaCode.setAdapter(adapter)
    }

    private fun initializeFireBaseAuth(view: View) {
        auth = Firebase.auth

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted: $credential")

                // Update the UI and attempt sign in with the phone credential
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.d(TAG, "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    binding.userPhoneNumberContainer.error = "Invalid phone number."
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                        // Administration error
                    Snackbar.make(view, "Quota exceeded.", Snackbar.LENGTH_SHORT).show()
                }

                // Show a message and update the UI
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:$verificationId")

                // Save verification ID and resending token so we can use them later
                storedVerificationId = verificationId

                // code was sent move user to next screen

                val action =
                    RegisterUserFragmentDirections.actionNavigationRegisterUserToNavigationVerifyPhoneNumber(
                        getUserPhoneNumber(),
                        storedVerificationId = storedVerificationId!!,
                        userFullName = getUserFullName(),
                        userId = getUserId()
                    )

                findNavController().navigate(action)
            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")

//                    val a = task.result.additionalUserInfo
//
//                    a.isNewUser


                    // create user profile
                    Utils.createUserProfile(
                        task.result.user, getUserFullName(), getUserId(), getUserPhoneNumber()
                    )

                    // redirect to mainActivity
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().finish()

                } else {
                    // Sign in failed, display a message and update the UI
                    Log.d(TAG, "signInWithCredential:failure", task.exception)

                    // Update UI
                    Snackbar.make(view!!, "Can't log in", Snackbar.LENGTH_INDEFINITE).show()
                }
            }
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)                // Phone number to verify
            .setTimeout(75L, TimeUnit.SECONDS)   // Timeout and unit
            .setActivity(requireActivity())             // Activity (for callback binding)
            .setCallbacks(callbacks)                    // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

        verificationInProgress = true
    }

    private fun getUserPhoneNumber(): String {
        return binding.areaCode.text.toString().trim() +
                binding.userPhoneNumber.text.toString().trim()
    }

    private fun getUserFullName() : String {
        return binding.userFullName.text.toString()
    }

    private fun getUserId(): String {
        return binding.userID.text.toString()
    }

    private fun updateUI() {
        // setup loading
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val TAG = "TAG"
    }
}