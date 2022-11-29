package org.xhanka.ndm_project.ui.dashboard.dashbord

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.xhanka.ndm_project.R
import org.xhanka.ndm_project.data.models.reporting.DashBoardMessage
import org.xhanka.ndm_project.ui.dashboard.DashBoardViewPagerFragmentDirections
import org.xhanka.ndm_project.utils.avatarBackground

class DashBoardMessagesAdapter(private val navController: NavController):
    ListAdapter<DashBoardMessage, DashBoardMessagesAdapter.ViewHolder>(DASH_COMP){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val parentContainer = itemView.findViewById<ConstraintLayout>(R.id.parentContainer)
        private val initials = itemView.findViewById<TextView>(R.id.contactInitials)
        private val fullName = itemView.findViewById<TextView>(R.id.contactFullName)
        private val lastMessage = itemView.findViewById<TextView>(R.id.lastMessage)
        private val messageTimeStamp = itemView.findViewById<TextView>(R.id.messageTimeStamp)

        fun bindToViewHolder(dashBoardMessage: DashBoardMessage, navController: NavController) {
            initials.text = dashBoardMessage.displayName.avatarBackground()
            fullName.text = dashBoardMessage.displayName
            lastMessage.text = dashBoardMessage.lastMessage
            messageTimeStamp.text = dashBoardMessage.timeStamp

            parentContainer.setOnClickListener {
                val action = DashBoardViewPagerFragmentDirections.goToEmergencyChat(
                    label = dashBoardMessage.displayName,
                    dashBoardMessage = dashBoardMessage
                )
                navController.navigate(action)
            }
         }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.dashboard_message_item, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindToViewHolder(getItem(position), navController)
    }

    companion object {
        val DASH_COMP = object: DiffUtil.ItemCallback<DashBoardMessage>() {
            override fun areItemsTheSame(
                oldItem: DashBoardMessage,
                newItem: DashBoardMessage
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DashBoardMessage,
                newItem: DashBoardMessage
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}