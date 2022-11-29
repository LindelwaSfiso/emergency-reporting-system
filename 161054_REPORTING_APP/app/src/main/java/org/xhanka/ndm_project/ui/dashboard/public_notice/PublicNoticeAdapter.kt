package org.xhanka.ndm_project.ui.dashboard.public_notice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.xhanka.ndm_project.R
import org.xhanka.ndm_project.data.models.reporting.PublicNotice
import org.xhanka.ndm_project.utils.Utils

class PublicNoticeAdapter: ListAdapter<PublicNotice, PublicNoticeAdapter.NoticeVH>(NOTICE_COMP) {
    class NoticeVH(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val noticeType = itemView.findViewById<ImageView>(R.id.noticeType)
        private val noticeMessage = itemView.findViewById<TextView>(R.id.noticeMessage)
        private val timeStamp = itemView.findViewById<TextView>(R.id.messageTimeStamp)
        private val container = itemView.findViewById<View>(R.id.parentContainer)

        fun bind(publicNotice: PublicNotice) {
            noticeMessage.text = publicNotice.message
            timeStamp.text = Utils.formatDate2(publicNotice.timeStamp.toDate())
            val icon = when (publicNotice.noticeType) {
                "MEDICAL" -> R.drawable.ic_medic
                "POLICE" -> R.drawable.ic_police
                else  -> R.drawable.ic_fire
            }
            noticeType.setImageResource(icon)
            container.setOnClickListener {
                AlertDialog.Builder(it.context)
                    .setIcon(icon)
                    .setTitle("Public Notice")
                    .setMessage(
                        "${publicNotice.message}\n\n==> ${Utils.formatDate(
                            publicNotice.timeStamp.toDate()
                        )}"
                    )
                    .setPositiveButton("OK", null)
                    .show()
            }
        }
    }
    companion object {
        val NOTICE_COMP = object: DiffUtil.ItemCallback<PublicNotice>() {
            override fun areItemsTheSame(oldItem: PublicNotice, newItem: PublicNotice): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: PublicNotice, newItem: PublicNotice): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeVH {
        return NoticeVH(LayoutInflater.from(parent.context).inflate(
            R.layout.public_notice_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NoticeVH, position: Int) {
        holder.bind(getItem(position))
    }
}