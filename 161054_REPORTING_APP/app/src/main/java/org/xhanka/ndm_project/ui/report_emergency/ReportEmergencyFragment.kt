package org.xhanka.ndm_project.ui.report_emergency

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.xhanka.ndm_project.databinding.FragmentReportEmergencyBinding

class ReportEmergencyFragment: Fragment() {

    private var _binding: FragmentReportEmergencyBinding ?= null
    private val binding get()= _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportEmergencyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}