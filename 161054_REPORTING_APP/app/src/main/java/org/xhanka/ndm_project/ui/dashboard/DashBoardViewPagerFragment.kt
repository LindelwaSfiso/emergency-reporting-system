package org.xhanka.ndm_project.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import org.xhanka.ndm_project.databinding.FragmentDashboardViewpagerBinding
import org.xhanka.ndm_project.ui.dashboard.dashbord.DashboardFragment
import org.xhanka.ndm_project.ui.dashboard.public_notice.PublicNoticeFragment
import org.xhanka.ndm_project.utils.SendSmsService

class DashBoardViewPagerFragment: Fragment(), MenuProvider {
    private var _binding: FragmentDashboardViewpagerBinding ?= null
    private val binding get() = _binding!!

    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardViewpagerBinding.inflate(inflater, container, false)
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ViewPagerAdapter(requireActivity())
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, index ->
            if (index == 0)
                tab.text = "Dash Board"
            else tab.text = "Public Notice"
        }.attach()
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

    class ViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            if(position == 0)
                return DashboardFragment.getInstance()
            return PublicNoticeFragment.getInstance()
        }

    }
}