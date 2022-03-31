package org.xhanka.ndm_project.ui.report_emergency

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import org.xhanka.ndm_project.databinding.FragmentReportEmergencyBinding
import java.util.*

@AndroidEntryPoint
class ReportEmergencyFragment : Fragment() {

    private var _binding: FragmentReportEmergencyBinding? = null
    private val binding get() = _binding!!

    private val reportEmergencyViewModel by viewModels<ReportEmergencyViewModel>()
    val args by navArgs<ReportEmergencyFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportEmergencyBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var textToSpeech: TextToSpeech
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textToSpeech = TextToSpeech(view.context) {
            Log.d("TAG", "INIT TEXT TO SPEECH ENGINE:\t$it")

            if (it != TextToSpeech.ERROR)
                textToSpeech.language = Locale.getDefault()
            if(it == TextToSpeech.SUCCESS)
                speak()
        }

        // Log.d("TAG", args.victimLocation.toString())
        args.victimLocation?.let {
            binding.displayLocation.text = // ℃
                String.format("Latitude:\t%f \nLongitude:\t %f", it.latitude, it.longitude)
        }

        reportEmergencyViewModel.countDown.observe(viewLifecycleOwner) {
            binding.countDown.text = it.toString()
        }

        binding.progressBar.isIndeterminate = false
        binding.progressBar.max = ReportEmergencyViewModel.TOTAL_TIME_SEC

        reportEmergencyViewModel.countDown.observe(viewLifecycleOwner) {
            binding.progressBar.progress = it
        }

        reportEmergencyViewModel.startCounter()
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

    override fun onPause() {
        super.onPause()
        reportEmergencyViewModel.stopCounter()
    }


    override fun onDestroy() {
        super.onDestroy()
        textToSpeech.stop()
        textToSpeech.shutdown()
        _binding = null
    }
}