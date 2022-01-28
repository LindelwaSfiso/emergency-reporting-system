package org.xhanka.ndm_project.ui.contacts

import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import org.xhanka.ndm_project.R
import org.xhanka.ndm_project.data.models.EmergencyContact
import org.xhanka.ndm_project.databinding.FragmentNewOrUpdateContactBinding

@AndroidEntryPoint
class NewOrUpdateContactFragment : Fragment() {

    private var _binding: FragmentNewOrUpdateContactBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<NewOrUpdateContactFragmentArgs>()
    private val emergencyContactViewModel by activityViewModels<EmergencyContactsViewModel>()

    //var a: ActivityResultLauncher<Void>? =null

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewOrUpdateContactBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController(view)

        args.emergencyContact?.let {

            // if contact is not null, update emergency contact
            updateEmergencyContact(it)

        } ?: run {

            // contact is null, create a new contact
            createNewEmergencyContact()

            // allow user to import contact
            setHasOptionsMenu(true)
        }

    }

    private fun updateEmergencyContact(contact: EmergencyContact) {

        binding.contactFullName.setText(contact.contactFullName)
        binding.contactPhoneNumber.setText(contact.contactPhoneNumber)

        binding.addOrUpdateContactButton.setOnClickListener {
            val contactFullName = binding.contactFullName.text.toString()
            val contactPhoneNumber = binding.contactPhoneNumber.text.toString()

            if (contactFullName.isNotEmpty() && contactPhoneNumber.isNotEmpty()) {
                contact.contactFullName = contactFullName
                contact.contactPhoneNumber = contactPhoneNumber
                emergencyContactViewModel.updateEmergencyContact(contact)
                navController.navigateUp()
            }
        }

    }

    private fun createNewEmergencyContact() {
        binding.addOrUpdateContactButton.setOnClickListener {
            val contactFullName = binding.contactFullName.text.toString()
            val contactPhoneNumber = binding.contactPhoneNumber.text.toString()

            if (contactFullName.isNotEmpty() && contactPhoneNumber.isNotEmpty()) {

                val contact = EmergencyContact(contactFullName, contactPhoneNumber)
                if (contact.isValidPhoneNumber())
                    emergencyContactViewModel.addNewEmergencyContact(contact)

                navController.navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    /*override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        Log.d("TAG", "ON CREATE MENU")
        menu.clear()
        inflater.inflate(R.menu.menu_add_contacts, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }*/

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_import_contact) {
            a?.launch(null)
        }
        return super.onOptionsItemSelected(item)
    }*/

    /*private fun setUpPickContactLauncher() {
       a = registerForActivityResult(ActivityResultContracts.PickContact()) { uri ->
            Log.d("TAG", uri.userInfo.toString())

            val cursor: Cursor? = requireActivity().contentResolver.query(
                uri, null,
                null, null, null
            )

            cursor?.let { it ->

                it.moveToFirst()

                try {

                    val displayNameIndex =
                        cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME)
                    val phoneNumberIndex =
                        cursor.getColumnIndexOrThrow("contact_status")

                    Log.d("TAG", "DISPLAY NAME" + cursor.getString(displayNameIndex))
                    Log.d("TAG", "PHONE NUMBER:\t" + cursor.getString(phoneNumberIndex))

                } catch (error: IllegalArgumentException) {
                    error.printStackTrace()
                    Toast.makeText(requireContext(), "An error occurred while trying to import contact", Toast.LENGTH_SHORT).show()
                }
            }

            cursor?.close()
        }
    }*/

}
