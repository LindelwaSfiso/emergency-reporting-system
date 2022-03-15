package org.xhanka.ndm_project.ui.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.xhanka.ndm_project.databinding.FragmentEmergencyContactsBinding

@AndroidEntryPoint
class EmergencyContactsFragment : Fragment() {

    private var _binding: FragmentEmergencyContactsBinding?= null
    private val binding get() = _binding!!

    private val emergencyContactViewModel by activityViewModels<EmergencyContactsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmergencyContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(view)

        binding.recyclerView.layoutManager = LinearLayoutManager(view.context)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(
            view.context, DividerItemDecoration.VERTICAL
        ))

        val adapter = EmergencyContactsAdapter(emergencyContactViewModel, navController)

        emergencyContactViewModel.emergencyContacts.observe(this, {
            if (it.isEmpty()) {
                binding.recyclerView.visibility = View.GONE
                binding.emptyView.visibility = View.VISIBLE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.emptyView.visibility = View.GONE

                adapter.submitList(it)
            }
        })

        binding.recyclerView.adapter = adapter

        binding.addOrUpdateContactButton.setOnClickListener {
            val action = EmergencyContactsFragmentDirections.actionEmergencyContactsToNavNewOrUpdate(
                label = "Add New Contact", null
            )
            navController.navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}