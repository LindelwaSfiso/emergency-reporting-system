package org.xhanka.ndm_project.ui.report_emergency.process

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import org.jetbrains.annotations.NotNull
import org.xhanka.ndm_project.R
import org.xhanka.ndm_project.databinding.FragmentConfirmCallBinding
import org.xhanka.ndm_project.utils.Utils

class ConfirmCallFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentConfirmCallBinding? = null
    private val binding get() = _binding!!
    var phoneNumber: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfirmCallBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        phoneNumber = arguments?.getSerializable("phoneNumber").toString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*val navController = requireActivity().let {
            Navigation.findNavController(it, R.id.nav_host_fragment_activity_main)
        }*/

        binding.callButton.setOnClickListener {
            Utils.launchCallIntent(
                phoneNumber, activity, { error ->
                    Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG)
                        .setAction("Grant") {
                            Utils.requestPermissionsFromSettings(view.context)
                        }.show()
                }) {
                requireActivity().finish()
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
        fun newInstance(phoneNumber: String): ConfirmCallFragment {
            val bundle = Bundle()
            bundle.putString("phoneNumber", phoneNumber)
            val fragment = ConfirmCallFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}