package org.xhanka.ndm_project.ui.dashboard.chat

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import org.xhanka.ndm_project.databinding.FragmentReportedEmergencyChatBinding
import org.xhanka.ndm_project.ui.dashboard.DashboardViewModel
import org.xhanka.ndm_project.utils.Utils

@AndroidEntryPoint
class ReportedEmergencyChatFragment : Fragment(), MenuProvider {
    private var _binding: FragmentReportedEmergencyChatBinding? = null
    private val binding get() = _binding!!
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private val args by navArgs<ReportedEmergencyChatFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportedEmergencyChatBinding.inflate(inflater, container, false)
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(view.context)
        layoutManager.stackFromEnd = true
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.addItemDecoration(Utils.VerticalSpace(10))

        val adapter = ReportedEmergencyChatAdapter()
        binding.recyclerView.adapter = adapter
        val conversationId = args.dashBoardMessage.getConversationId(
            Firebase.auth.currentUser!!.uid
        )

        dashboardViewModel.conversationMessages.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        // subscribe to conversation, with conversationId
        dashboardViewModel.subscribeToUserConversation(conversationId)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }
}