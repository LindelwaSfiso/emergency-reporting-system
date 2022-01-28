package org.xhanka.ndm_project.ui.contacts

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.xhanka.ndm_project.R
import org.xhanka.ndm_project.data.models.EmergencyContact


/**
 * Adapter for display a list of personal emergency contact numbers the user has optionally
 * added
 *
 * @author Dlamini Lindelwa
 */
class EmergencyContactsAdapter(private val viewModel: EmergencyContactsViewModel, private val navController: NavController) :
    ListAdapter<EmergencyContact, EmergencyContactsAdapter.EmergencyContactsVH>(
        CONTACT_COMPARATOR
    ) {
    class EmergencyContactsVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var parentContainer: ConstraintLayout = itemView.findViewById(R.id.parentContainer)
        var contactInitials: TextView = itemView.findViewById(R.id.contactInitials)
        var contactFullName: TextView = itemView.findViewById(R.id.contactFullName)
        var contactPhoneNumber: TextView = itemView.findViewById(R.id.contactPhoneNumber)
    }

    companion object {
        val CONTACT_COMPARATOR = object : DiffUtil.ItemCallback<EmergencyContact>() {
            override fun areItemsTheSame(
                oldItem: EmergencyContact,
                newItem: EmergencyContact
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: EmergencyContact,
                newItem: EmergencyContact
            ): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmergencyContactsVH {
        return EmergencyContactsVH(LayoutInflater.from(parent.context).inflate(
            R.layout.emergency_contact_item, parent,false
        ))
    }

    override fun onBindViewHolder(holder: EmergencyContactsVH, position: Int) {
        val contact = getItem(position)

        holder.contactFullName.text = contact.contactFullName
        holder.contactPhoneNumber.text = contact.contactPhoneNumber
        holder.contactInitials.text = contact.contactFullName.firstOrNull().toString().uppercase()

        holder.parentContainer.setOnLongClickListener {
            AlertDialog.Builder(it.context)
                .setTitle("Remove Contact")
                .setMessage("Are you sure you want to remove this contact number? This action is not reversible!")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Remove") { _, _ ->
                    viewModel.deleteEmergencyContact(contact)
                }.create().show()

            true
        }

        holder.parentContainer.setOnClickListener {
            val action = EmergencyContactsFragmentDirections.actionEmergencyContactsToNavNewOrUpdate(
                "Edit Contact", contact
            )
            navController.navigate(action)
        }
    }
}