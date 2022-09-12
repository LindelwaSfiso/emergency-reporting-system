package org.xhanka.ndm_project.ui.registration

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.xhanka.ndm_project.MainActivity
import org.xhanka.ndm_project.databinding.FragmentVerifyPhoneNumberBinding
import org.xhanka.ndm_project.utils.Utils
import java.util.concurrent.TimeUnit

class VerifyPhoneNumberFragment : Fragment() {

    private var _binding: FragmentVerifyPhoneNumberBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private var storedVerificationId: String? = ""
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    private val args by navArgs<VerifyPhoneNumberFragmentArgs>()

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

        initializeFireBaseAuth(view)

        storedVerificationId = args.storedVerificationId

        binding.verifyPhoneNumber.setOnClickListener {
            val code = binding.verificationCode.text.toString()

            if (TextUtils.isEmpty(code)) {
                binding.verificationCodeContainer.error = "Cannot be empty."
                return@setOnClickListener
            }

            verifyPhoneNumberWithCode(storedVerificationId, code)
        }

        binding.resendVerificationCode.setOnClickListener {
            resendVerificationCode(args.userPhoneNumber)
        }
    }

    private fun initializeFireBaseAuth(view: View) {
        auth = Firebase.auth

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted: $credential")
                // Update the UI and attempt sign in with the phone credential
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.d(TAG, "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    binding.verificationCodeContainer.error = "Invalid phone number."
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
                Log.d(TAG, "onCodeSent:$verificationId")
                storedVerificationId = verificationId
            }
        }
    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")

                    // user is validated, create profile
                    Utils.createUserProfile(
                        task.result.user,
                        args.userFullName,
                        args.userId,
                        args.userPhoneNumber
                    )

                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().finish()
                    return@addOnCompleteListener

                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        binding.verifyPhoneNumber.error = "Invalid code."
                    }
                }
            }
    }

    private fun resendVerificationCode(
        phoneNumber: String
    ) {
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(75L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks

        // removed force resend token

        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }

    private fun countDownTimer() = object : CountDownTimer(75_000, 1000) {

        override fun onTick(millisUntilFinished: Long) {
            Log.d("TAG", millisUntilFinished.toString())
        }

        override fun onFinish() {
            binding.resendVerificationCode.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val TAG = "TAG"
    }
}