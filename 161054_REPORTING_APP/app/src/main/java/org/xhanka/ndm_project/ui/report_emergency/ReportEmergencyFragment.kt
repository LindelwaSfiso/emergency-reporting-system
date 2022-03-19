package org.xhanka.ndm_project.ui.report_emergency

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.xhanka.ndm_project.databinding.FragmentReportEmergencyBinding
import org.xhanka.ndm_project.ui.home.HomeViewModel
import java.util.*

@AndroidEntryPoint
class ReportEmergencyFragment : Fragment() {

    private var _binding: FragmentReportEmergencyBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel by activityViewModels<HomeViewModel>()
    private val reportEmergencyViewModel by viewModels<ReportEmergencyViewModel>()

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
            if (it != TextToSpeech.ERROR)
                textToSpeech.language = Locale.getDefault()
        }

        speak()

        homeViewModel.currentLocation.observe(viewLifecycleOwner) {
            binding.displayLocation.text =
                String.format("Latitude:\t%f\nLongitude \u2103:\t%f", it.latitude, it.longitude)
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