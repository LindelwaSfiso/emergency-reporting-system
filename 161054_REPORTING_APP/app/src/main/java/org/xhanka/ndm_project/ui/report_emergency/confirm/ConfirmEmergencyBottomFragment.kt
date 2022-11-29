package org.xhanka.ndm_project.ui.report_emergency.confirm

import android.annotation.SuppressLint
import android.app.Dialog
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.jetbrains.annotations.NotNull
import org.xhanka.ndm_project.R
import org.xhanka.ndm_project.databinding.FragmentConfirmEmergencyBinding
import org.xhanka.ndm_project.ui.report_emergency.report.ReportEmergencyFragmentDirections
import org.xhanka.ndm_project.utils.Utils


class ConfirmEmergencyBottomFragment: BottomSheetDialogFragment () {
    private var _binding: FragmentConfirmEmergencyBinding? = null
    private val binding get() = _binding!!
    var location: Location? = null
    var translatedText: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfirmEmergencyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        location = arguments?.getParcelable("victimLocation")
        translatedText = arguments?.getString("translatedText", "") ?: ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val type = Utils.searchForKeyWords(translatedText)

        binding.emergencyType.text = type
        binding.translatedText.text = translatedText

        val navController = activity?.let {
            Navigation.findNavController(it, R.id.nav_host_fragment_activity_main)
        }

        binding.contactEmergencies.isEnabled = type != "NO MATCHES, TRY AGAIN"

        binding.contactEmergencies.setOnClickListener {
            navController?.apply {
                navigate(ReportEmergencyFragmentDirections.actionNavigateToProcessEmergency(
                    location, type
                ))
            }
        }
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }

    @NonNull
    @NotNull
    @SuppressLint("RestrictedApi", "VisibleForTests")
    override fun onCreateDialog(@Nullable savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.behavior.disableShapeAnimations()
        return bottomSheetDialog
    }

    companion object {
        fun newInstance(location: Location?, translatedText: String): ConfirmEmergencyBottomFragment {
            val bundle = Bundle()
            bundle.putParcelable("victimLocation", location)
            bundle.putString("translatedText", translatedText)
            val fragment = ConfirmEmergencyBottomFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}