package org.xhanka.ndm_project.ui.dashboard.public_notice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.xhanka.ndm_project.R
import org.xhanka.ndm_project.databinding.FragmentPublicNoticeBinding
import org.xhanka.ndm_project.ui.dashboard.DashboardViewModel
import org.xhanka.ndm_project.utils.SendSmsService

class PublicNoticeFragment: Fragment(){
    private var _binding: FragmentPublicNoticeBinding ?= null
    private val binding get() = _binding!!
    private val dashboardViewModel by activityViewModels<DashboardViewModel>()
    private lateinit var notificationManager: NotificationManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPublicNoticeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notificationManager = view.context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        dashboardViewModel.subscribeToPublicNotice()
        val adapter = PublicNoticeAdapter()

        binding.recyclerView.layoutManager = LinearLayoutManager(view.context)
        binding.recyclerView.addItemDecoration(DividerItemDecoration (
            view.context, DividerItemDecoration.VERTICAL
        ))

        dashboardViewModel.publicNotice.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        binding.recyclerView.adapter = adapter

        dashboardViewModel.displayNotifications.observe(viewLifecycleOwner) {
            if (it) {
                notificationManager.notify(
                    NOTIFICATION_ID,
                    createNotification().build()
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun createNotification(): NotificationCompat.Builder {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                SendSmsService.NOTIFICATION_CHANNEL_ID,
                "New Messages",
                NotificationManager.IMPORTANCE_HIGH
            ).let {
                it.description = "New messages"
                it.enableLights(true)
                it.lightColor = Color.RED
                it.enableVibration(false)
                it
            }
            notificationManager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(requireContext(), SendSmsService.NOTIFICATION_CHANNEL_ID)
            .setVibrate(null)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle("New messages")
            .setContentText("You have new public notice notifications.")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            //.setSilent(true)
            .setTimeoutAfter(-1)
            .setAutoCancel(false)
    }

    companion object {
        const val NOTIFICATION_ID = 12

        @Volatile
        private lateinit var instance: PublicNoticeFragment

        fun getInstance(): PublicNoticeFragment {
            synchronized(this) {
                if (!Companion::instance.isInitialized) {
                    instance = PublicNoticeFragment()
                }
                return instance
            }
        }
    }
}