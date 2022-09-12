package org.xhanka.ndm_project.ui.report_emergency

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import org.xhanka.ndm_project.R
import org.xhanka.ndm_project.databinding.FragmentReportEmergencyBinding
import org.xhanka.ndm_project.utils.EmergencyType

@AndroidEntryPoint
class ReportEmergencyFragment : Fragment() {

    private var _binding: FragmentReportEmergencyBinding? = null
    private val binding get() = _binding!!

    private val reportEmergencyViewModel by viewModels<ReportEmergencyViewModel>()
    private val args by navArgs<ReportEmergencyFragmentArgs>()

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

        args.victimLocation?.let {
            binding.currentLocationText.text = // ℃
                    // String.format("Latitude:\t%f \nLongitude:\t %f", it.latitude, it.longitude)
                String.format("(${it.latitude}, ${it.longitude})")
        }

        var checked: Int
        var emergencyType: EmergencyType = EmergencyType.FIRE
        binding.emergencyType.setOnCheckedChangeListener { _, checkedId ->
            checked = checkedId
            when(checked) {
                R.id.medicalDepartment -> emergencyType = EmergencyType.MEDICAL
                R.id.fireDepartment -> emergencyType = EmergencyType.FIRE
                R.id.policeDepartment -> emergencyType = EmergencyType.POLICE
            }
        }

        binding.proceedButton.setOnClickListener{
            val a = Toast.makeText(
                it.context, "Selected id:\t${emergencyType.value}", Toast.LENGTH_SHORT
            )
            a.setGravity(Gravity.TOP, 0,0)
            a.show()
        }

    }

    override fun onPause() {
        super.onPause()
        reportEmergencyViewModel.stopCounter()
    }


    override fun onDestroy() {
        super.onDestroy()
        /*textToSpeech.stop()
        textToSpeech.shutdown()*/
        _binding = null
    }
}

/*  lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var textToSpeech: TextToSpeech
    lateinit var speechRecognizerIntent: Intent
*/

/*        textToSpeech = TextToSpeech(view.context) {
            Log.d("TAG", "INIT TEXT TO SPEECH ENGINE:\t$it")

            if (it != TextToSpeech.ERROR)
                textToSpeech.language = Locale.getDefault()
            if(it == TextToSpeech.SUCCESS)
                speak()
        }*/

/*//        reportEmergencyViewModel.countDown.observe(viewLifecycleOwner) {
//            binding.countDown.text = it.toString()
//        }

        binding.progressBar.isIndeterminate = false
        binding.progressBar.max = ReportEmergencyViewModel.TOTAL_TIME_SEC

        reportEmergencyViewModel.countDown.observe(viewLifecycleOwner) {
            binding.progressBar.progress = it
        }*/

/*     reportEmergencyViewModel.startCounter()


        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(view.context)
        speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            Locale.ENGLISH
        )

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}

            override fun onBeginningOfSpeech() {
                Log.d("TAG", "ON BEGINNING OF SPEECH")
            }

            override fun onRmsChanged(rmsdB: Float) {}

            override fun onBufferReceived(buffer: ByteArray?) {
                Log.d("TAG", "ON BUFFER RECEIVED")
            }

            override fun onEndOfSpeech() {
                Log.d("TAG", "ON END OF SPEECH")
            }

            override fun onError(error: Int) {
                Log.d("TAG", "ERROR:\t$error")

                when (error){
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT, SpeechRecognizer.ERROR_NETWORK, SpeechRecognizer.ERROR_SERVER -> {
                        val packageName = "com.google.android.googlequicksearchbox"
                        val activityInstallLanguage = "com.google.android.voicesearch.greco3.languagepack.InstallActivity"

                        val installLanguageIntent = Intent()
                        installLanguageIntent.component = ComponentName(
                            packageName, activityInstallLanguage
                        )
                        installLanguageIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TOP

                        try {
                            // launch install offline
                            view.context.startActivity(installLanguageIntent)
                        } catch (exception: Exception) {
                            // for android 10 and below
                            Log.d("TAG", "${exception.localizedMessage?.toString()}")
                        }
                    }
                    else -> return
                }
            }

            override fun onResults(results: Bundle?) {
                Log.d("TAG", "ON RESULTS")
                val result = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (result != null) {
                    binding.displayLocation.text = result[0]
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
                Log.d("TAG", "ON PARTIAL RESULTS")
                val result = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (result != null) {
                    Log.d("TAG", result[0])
                }
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                Log.d("TAG", "ON EVENT:\t$eventType")
                val result = params?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (result != null) {
                    Log.d("TAG", result[0])
                }
            }
        })

        speechToText(binding.proceedButton)*/

/*    @SuppressLint("ClickableViewAccessibility")
    private fun speechToText(button: MaterialButton) {
        button.setOnTouchListener { _, event ->

            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    // speechRecognizer.stopListening()
                    // binding.displayLocation.text = "Done recording..."
                    true
                }

                MotionEvent.ACTION_DOWN -> {
                    binding.displayLocation.text = "Recording..."
                    speechRecognizer.startListening(speechRecognizerIntent)
                    true
                }

                else -> false
            }
        }
    }

    private fun speak() {
        textToSpeech.speak(
            "This is 911. How can we help you? Reply with the following commands.",
            TextToSpeech.QUEUE_FLUSH,
            null,
            null
        )

        textToSpeech.playSilentUtterance(
            100,
            TextToSpeech.QUEUE_ADD,
            TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID
        )

        textToSpeech.speak(
            "1, for Police." +
                    "2, form Medical." +
                    "and 3, for the fire department!",
            TextToSpeech.QUEUE_ADD,
            null,
            null
        )
    }

    private fun checkRecordAudioPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + context.packageName)
                )
                binding.displayLocation.text = "Permission Denied..."
                startActivity(intent)
            }
        }
    }*/
