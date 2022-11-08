package org.xhanka.ndm_project.ui.contacts.first_responder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.xhanka.ndm_project.R
import org.xhanka.ndm_project.data.models.contacts.EmergencyStation
import org.xhanka.ndm_project.utils.avatarBackground

class EmergencyStationAdapter:
    ListAdapter<EmergencyStation, EmergencyStationAdapter.EmergencyStationVH>(STATION_COMPARATOR)
{
    class EmergencyStationVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var contactInitials: TextView = itemView.findViewById(R.id.contactInitials)
        var contactFullName: TextView = itemView.findViewById(R.id.contactFullName)
        var contactPhoneNumber: TextView = itemView.findViewById(R.id.contactPhoneNumber)

        fun bind(station: EmergencyStation) {
            contactInitials.text = station.stationName.avatarBackground()
            contactFullName.text = station.stationName
            contactPhoneNumber.text = station.stationTelephone
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmergencyStationVH {
        return EmergencyStationVH(LayoutInflater.from(parent.context).inflate(
                R.layout.emergency_contact_item, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: EmergencyStationVH, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val STATION_COMPARATOR = object: DiffUtil.ItemCallback<EmergencyStation> () {
            override fun areItemsTheSame(
                oldItem: EmergencyStation,
                newItem: EmergencyStation
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: EmergencyStation,
                newItem: EmergencyStation
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}