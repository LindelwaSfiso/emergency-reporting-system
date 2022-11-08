package org.xhanka.ndm_project.ui.contacts

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.xhanka.ndm_project.R
import org.xhanka.ndm_project.databinding.FragmentEmergencyContactsBinding
import org.xhanka.ndm_project.ui.contacts.personal.EmergencyContactsAdapter
import org.xhanka.ndm_project.ui.contacts.personal.EmergencyContactsViewModel

@AndroidEntryPoint
class EmergencyContactsFragment : Fragment(), MenuProvider {

    private var _binding: FragmentEmergencyContactsBinding?= null
    private val binding get() = _binding!!

    private val emergencyContactViewModel by activityViewModels<EmergencyContactsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmergencyContactsBinding.inflate(inflater, container, false)
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
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

        emergencyContactViewModel.emergencyContacts.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.recyclerView.visibility = View.GONE
                binding.emptyView.visibility = View.VISIBLE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.emptyView.visibility = View.GONE

                val sorted = it.sortedBy { contact -> contact.contactFullName }
                adapter.submitList(sorted)
            }
        }

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

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_emergency_stations, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.menu_emergency_stations){
            findNavController().navigate(R.id.navigation_emergency_stations)
            return true
        }
        return false
    }
}