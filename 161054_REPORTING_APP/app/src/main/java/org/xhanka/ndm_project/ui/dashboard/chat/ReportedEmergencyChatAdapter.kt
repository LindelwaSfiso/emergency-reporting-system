package org.xhanka.ndm_project.ui.dashboard.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.xhanka.ndm_project.R
import org.xhanka.ndm_project.data.models.reporting.EmergencyMessage

class ReportedEmergencyChatAdapter :
    ListAdapter<EmergencyMessage, ReportedEmergencyChatAdapter.EmergencyChatVH>(CHAT_COMP) {
    class EmergencyChatVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val chatMessage: TextView = itemView.findViewById(R.id.chat_message)
        private val chatTimeStamp: TextView = itemView.findViewById(R.id.chat_timestamp)

        fun bind(emergencyMessage: EmergencyMessage) {
            if (emergencyMessage.emergencyMessageBody.isNotBlank())
                chatMessage.text = emergencyMessage.emergencyMessageBody
            if (emergencyMessage.timeStamp.isNotBlank())
                chatTimeStamp.text = emergencyMessage.timeStamp
        }
    }

    companion object {
        val CHAT_COMP = object : DiffUtil.ItemCallback<EmergencyMessage>() {
            override fun areItemsTheSame(
                oldItem: EmergencyMessage,
                newItem: EmergencyMessage
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: EmergencyMessage,
                newItem: EmergencyMessage
            ): Boolean {
                return oldItem == newItem
            }

        }
        const val VIEW_TYPE_SENDER = 1
        const val VIEW_TYPE_RECEIVER = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmergencyChatVH {
        val layoutId = if (viewType == VIEW_TYPE_RECEIVER)
            R.layout.reported_emergency_chat_receive_item
        else R.layout.reported_emergency_chat_send_item

        return EmergencyChatVH(
            LayoutInflater.from(parent.context).inflate(
                layoutId, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: EmergencyChatVH, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        if (getItem(position).senderUid != Firebase.auth.currentUser!!.uid)
            return VIEW_TYPE_RECEIVER
        return VIEW_TYPE_SENDER
    }
}