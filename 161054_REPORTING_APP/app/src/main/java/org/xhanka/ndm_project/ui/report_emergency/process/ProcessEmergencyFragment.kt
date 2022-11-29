package org.xhanka.ndm_project.ui.report_emergency.process

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import org.xhanka.ndm_project.data.models.contacts.EmergencyStation
import org.xhanka.ndm_project.databinding.FragmentProcessEmergencyBinding
import org.xhanka.ndm_project.ui.settings.UserProfileFragment
import org.xhanka.ndm_project.utils.SendSmsService
import org.xhanka.ndm_project.utils.parseCoordinates
import java.util.*

@AndroidEntryPoint
class ProcessEmergencyFragment : Fragment() {
    private var _binding: FragmentProcessEmergencyBinding ?= null
    private val binding get() = _binding!!

    private val args by navArgs<ProcessEmergencyFragmentArgs>()
    private val viewModel by viewModels<ProcessEmergencyViewModel>()

    private var fullName = ""
    private var locationString = ""
    private var userName = ""
    lateinit var location: Location
    lateinit var emergencyStations: List<EmergencyStation>
    var emergencyType = 10 // default medical

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProcessEmergencyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Flow.
        // 2. Search for closest station, create algorithm for this (efficiency)
        // 3. Start sending sms in background
        // 4. Send notification to emergency services
        // 5. Redirect and initiate call

        args.victimLocation?.let {
            location = it
            locationString = "(${it.latitude}, ${it.longitude})"
            binding.location.text = String.format("(${it.latitude}, ${it.longitude})")
        }

        args.emergencyType?.let {
            emergencyType = when (it) { "MEDICAL" -> 10; "POLICE" -> 11; else -> 12 }
            binding.emergencyType.text = it.uppercase()
        }

        binding.startProcessing.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            binding.warningText.text = String.format("Please wait while we process your emergency!")

            // 1. first step
            stepOneInitiateSms(fullName, locationString)

            // 2. second step
            val station = stepTwoLocatingNearestStation()

            // 3. third step, send emergency
            viewModel.sendEmergencyNotificationToServices(
                userId = Firebase.auth.currentUser!!.uid,
                emergencyStationId = station.uid,
                stationName = station.stationName,
                userName = userName,
                coordinates = location,  //"$fullName -- $locationString",
                emergencyMessageBody = String.format(Locale.ENGLISH, DEFAULT_MESSAGE, fullName),
                emergencyType = emergencyType.toString()
            )
        }

        viewModel.user.observe(viewLifecycleOwner) { user ->
            binding.fullName.text = user.fullName.trim()
            binding.phoneNumber.text = user.phoneNumber.trim()
            fullName = user.fullName.trim()
            userName = user.fullName.trim()
        }

        viewModel.allEmergencyStations.observe(viewLifecycleOwner) {
            emergencyStations = it
        }

        viewModel.done.observe(viewLifecycleOwner) {
            if (it) {
                // done. hide progress bar
                binding.progressBar.visibility = View.INVISIBLE
                binding.stepThree.isChecked = true
                showCallDialog()
            }
        }
    }

    private fun showCallDialog() {
        val confirmCall = ConfirmCallFragment.newInstance("76480479")
        confirmCall.show(childFragmentManager, "confirm_call")
    }

    private fun stepOneInitiateSms(fullName: String, location: String) {
        val intent = Intent(requireContext(), SendSmsService::class.java).apply {
            action = SendSmsService.ACTION_SEND_SMS
        }
        intent.putExtra("fullName", fullName)
        intent.putExtra("location", location)
        requireActivity().startService(intent)
        binding.stepOne.isChecked = true
    }

    private fun stepTwoLocatingNearestStation(): EmergencyStation {
        Log.d("TAG", emergencyStations.toString() + "\t" + emergencyType)
        val relevantStations = emergencyStations.filter {
            it.stationType == emergencyType
        }

        var shortestDistance = 0.0f
        var foundStation: EmergencyStation = relevantStations.first()
        relevantStations.forEachIndexed {index, it ->
            Log.d("TAG", it.stationName)
            val coordinates = it.stationCoordinates.parseCoordinates()
            val results = FloatArray(5)
            Location.distanceBetween(
                location.latitude, location.longitude, coordinates.first, coordinates.second, results
            )

            if (index == 0) { shortestDistance = results[0] }

            if (shortestDistance > results[0]) {
                foundStation = it
                shortestDistance = results[0]
                Log.d("TAG", "D: ${results[0]} m")
            }
        }

        Log.d("TAG", "FOUND STATION $foundStation  Distance: $shortestDistance")
        binding.stepTwo.isChecked = true

        // if nothing was found, stop program, this is highly unlikely
        binding.foundStation.text = foundStation.stationName
        binding.foundStation.visibility = View.VISIBLE
        binding.helperFoundStation.visibility = View.VISIBLE
        return foundStation
    }

    companion object {
        const val DEFAULT_MESSAGE = "%s is reporting an emergency. Find attached coordinates to location."
    }
}