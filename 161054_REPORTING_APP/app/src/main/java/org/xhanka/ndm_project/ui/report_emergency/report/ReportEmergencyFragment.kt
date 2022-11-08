package org.xhanka.ndm_project.ui.report_emergency.report

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.xhanka.ndm_project.databinding.FragmentReportEmergencyBinding
import org.xhanka.ndm_project.ui.dashboard.TapHoldUpButton
import org.xhanka.ndm_project.ui.report_emergency.confirm.ConfirmEmergencyBottomFragment
import org.xhanka.ndm_project.utils.Utils

@AndroidEntryPoint
class ReportEmergencyFragment : Fragment() {

    private var _binding: FragmentReportEmergencyBinding? = null
    private val binding get() = _binding!!

    private val reportEmergencyViewModel by activityViewModels<ReportEmergencyViewModel>()
    private val args by navArgs<ReportEmergencyFragmentArgs>()

    private var location: Location? = null
    private var translatedText: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportEmergencyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reportEmergencyViewModel.initializeRecognizer(view.context)

        args.victimLocation?.let {
            location = it
            binding.currentLocationText.text =
                String.format("(${it.latitude}, ${it.longitude})")
        }

        binding.tapAndHold.setOnButtonClickListener(object : TapHoldUpButton.OnButtonClickListener {
            override fun onLongHoldStart(v: View?) {
                binding.progressBar.visibility = VISIBLE
                if (checkPermissions(view.context))
                    reportEmergencyViewModel.startListening()
                else Utils.requestPermissionsFromSettings(view.context)
            }

            override fun onLongHoldEnd(v: View?) {
                binding.progressBar.visibility = INVISIBLE
                if (checkPermissions(view.context))
                    reportEmergencyViewModel.stopListening()
                else Utils.requestPermissionsFromSettings(view.context)
            }

            override fun onClick(v: View?) {
                // Toast.makeText(requireContext(), "Click and hold", Toast.LENGTH_SHORT).show()
                // val confirmDialog = ConfirmEmergencyBottomFragment.newInstance(location, "")
                // confirmDialog.show(childFragmentManager, "confirm_dialog")
            }
        })


        reportEmergencyViewModel.results.observe(viewLifecycleOwner) { results: String? ->
            Log.d("TAG", "RESULTS $results")

            results?.let {
                if (results.isNotEmpty()) {
                    val confirmDialog =
                        ConfirmEmergencyBottomFragment.newInstance(location, results)
                    confirmDialog.show(childFragmentManager, "confirm_dialog")
                }
            }
        }


        reportEmergencyViewModel.errors.observe(viewLifecycleOwner) {
            if (it?.isNotBlank() == true) {
                Snackbar.make(view, it, Snackbar.LENGTH_LONG).show()
                reportEmergencyViewModel.clearErrors()
            }
        }
    }

    private fun checkPermissions(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context, Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        reportEmergencyViewModel.destroy()
    }
}

/*
* \package org.xhanka.ndm_project.ui.report_emergency.report

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.google.android.gms.auth.api.phone.SmsCodeAutofillClient.PermissionState.GRANTED
import dagger.hilt.android.AndroidEntryPoint
import org.vosk.Model
import org.vosk.android.StorageService
import org.xhanka.ndm_project.databinding.FragmentReportEmergencyBinding
import org.xhanka.ndm_project.ui.dashboard.TapHoldUpButton
import org.xhanka.ndm_project.ui.report_emergency.confirm.ConfirmEmergencyBottomFragment
import org.xhanka.ndm_project.utils.Utils
import java.util.*

@AndroidEntryPoint
class ReportEmergencyFragment : Fragment() {

    private var _binding: FragmentReportEmergencyBinding? = null
    private val binding get() = _binding!!

    private val reportEmergencyViewModel by activityViewModels<ReportEmergencyViewModel>()
    private val args by navArgs<ReportEmergencyFragmentArgs>()

    private var location: Location? = null
    private var translatedText: String = ""

    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var speechRecognizerIntent: Intent

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportEmergencyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(view.context)
        speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

        args.victimLocation?.let {
            location = it
            binding.currentLocationText.text =
                String.format("(${it.latitude}, ${it.longitude})")
        }

        binding.tapAndHold.setOnButtonClickListener(object : TapHoldUpButton.OnButtonClickListener {
            override fun onLongHoldStart(v: View?) {
                binding.progressBar.visibility = VISIBLE
                //reportEmergencyViewModel.pause(false)
                speechRecognizer.startListening(speechRecognizerIntent)
            }

            override fun onLongHoldEnd(v: View?) {
                binding.progressBar.visibility = INVISIBLE
                //reportEmergencyViewModel.pause(true)
                speechRecognizer.stopListening()
            }

            override fun onClick(v: View?) {
                // Toast.makeText(requireContext(), "Click and hold", Toast.LENGTH_SHORT).show()
                val confirmDialog = ConfirmEmergencyBottomFragment.newInstance(location, "")
                confirmDialog.show(childFragmentManager, "confirm_dialog")
            }
        })

        reportEmergencyViewModel.state.observe(viewLifecycleOwner) { state ->
            Log.d("TAG", state.toString())
        }

        reportEmergencyViewModel.results.observe(viewLifecycleOwner) { results ->
            Log.d("TAG", "RESULTS $results")

            if (results.isNotEmpty()) {
                val confirmDialog = ConfirmEmergencyBottomFragment.newInstance(location, results)
                confirmDialog.show(childFragmentManager, "confirm_dialog")
                reportEmergencyViewModel.clearTranslatedText()
            }
        }

        // initialize model, start unpacking
        // initModel()

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(view.context)
        speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(p0: Bundle?) {
                Log.d("TAG", "ON READY FOR SPEECH")
            }

            override fun onBeginningOfSpeech() {
                Log.d("TAG", "ON BEGINNING OF SPEECH")
            }

            override fun onRmsChanged(p0: Float) {
                Log.d("TAG", "ON RMS CHANGED")
            }

            override fun onBufferReceived(p0: ByteArray?) {
                Log.d("TAG", "ON BUFFER RECEIVED")
            }

            override fun onEndOfSpeech() {
                Log.d("TAG", "ON END OF SPEECH")
            }

            override fun onError(p0: Int) {
                Log.d("TAG", "ON ON ERROR")
            }

            override fun onResults(p0: Bundle?) {
                val r = p0?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                Log.d("TAG", "ON RESULTS $r")
            }

            override fun onPartialResults(p0: Bundle?) {
                Log.d("TAG", "ON partial results")
            }

            override fun onEvent(p0: Int, p1: Bundle?) {
                Log.d("TAG", "ON event p0$p0 p1${p1.toString()}")
            }
        })
    }

    private fun checkPermissions(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) ==
                PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        reportEmergencyViewModel.onDestroy()
        _binding = null
        speechRecognizer.destroy()
    }

    private fun initModel() {
        StorageService.unpack(
            requireContext(),
            "model-en-us",
            "model", { model: Model? ->
                reportEmergencyViewModel.setUpModel(model)
                reportEmergencyViewModel.setState(ReportEmergencyViewModel.STATE.STATE_READY)
                // if successfully loaded model, start listening
                reportEmergencyViewModel.startMicrophoneRecognition()
            }, {
                reportEmergencyViewModel.setState(ReportEmergencyViewModel.STATE.STATE_UNPACKING)
            }
        )
    }
}

*
* */