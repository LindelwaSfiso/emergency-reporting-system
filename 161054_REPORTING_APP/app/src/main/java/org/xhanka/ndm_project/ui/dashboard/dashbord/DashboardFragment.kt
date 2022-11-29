package org.xhanka.ndm_project.ui.dashboard.dashbord

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.xhanka.ndm_project.databinding.FragmentDashboardBinding
import org.xhanka.ndm_project.ui.dashboard.DashboardViewModel


class DashboardFragment : Fragment() {

    private val dashboardViewModel by activityViewModels<DashboardViewModel>()
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(view.context)
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                view.context, DividerItemDecoration.VERTICAL)
        )
        binding.recyclerView.setHasFixedSize(true)

        val adapter = DashBoardMessagesAdapter(findNavController())
        binding.recyclerView.adapter = adapter

        dashboardViewModel.dashBoardMessages.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }

        dashboardViewModel.subscribeToUserDashBoard(Firebase.auth.currentUser!!.uid)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @Volatile
        private lateinit var instance: DashboardFragment

        fun getInstance(): DashboardFragment {
            synchronized(this) {
                if (!Companion::instance.isInitialized) {
                    instance = DashboardFragment()
                }
                return instance
            }
        }
    }
}
