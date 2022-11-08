package org.xhanka.ndm_project.ui.contacts.first_responder

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.xhanka.ndm_project.R
import org.xhanka.ndm_project.databinding.FragmentEmergencyStationsBinding

@AndroidEntryPoint
class EmergencyStationsFragment: Fragment(), MenuProvider {
    private var _binding: FragmentEmergencyStationsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<EmergencyStationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmergencyStationsBinding.inflate(inflater, container, false)
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(view.context)
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL)
        )

        val adapter = EmergencyStationAdapter()

        viewModel.emergencyStations.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.recyclerView.visibility = View.INVISIBLE
                binding.emptyView.visibility = View.VISIBLE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.emptyView.visibility = View.GONE
                adapter.submitList(it)
                it[0].let { x ->
                    Log.d("TAG", "NAME:\t" + x.stationName)
                    Log.d("TAG", "COORDINATES:\t" + x.stationCoordinates)
                    Log.d("TAG", "TELEPHONE:\t" + x.stationTelephone)
                }
            }
        }
        binding.recyclerView.adapter = adapter
        viewModel.loading.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = if(it) View.VISIBLE else View.GONE
        }
        viewModel.errorState.observe(viewLifecycleOwner) {
            it?.let { message ->
                if (message.isNotEmpty())
                    Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
            }
            viewModel.resetMessage()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.emergency_station_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if(menuItem.itemId == R.id.manuallySyncDatabase) {
            AlertDialog.Builder(requireContext())
                .setTitle("Sync")
                .setMessage("Manually sync first responder database?")
                .setPositiveButton("Sync") { _, _->
                    viewModel.retrieveFirstResponderDatabase()
                }.show()
            return true
        }
        return false
    }

}