package org.xhanka.ndm_project.ui.contacts

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import org.xhanka.ndm_project.R
import org.xhanka.ndm_project.data.models.EmergencyContact
import org.xhanka.ndm_project.databinding.FragmentNewOrUpdateContactBinding
import org.xhanka.ndm_project.utils.Utils
import java.security.Permissions

@AndroidEntryPoint
class NewOrUpdateContactFragment : Fragment() {

    private var _binding: FragmentNewOrUpdateContactBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<NewOrUpdateContactFragmentArgs>()
    private val emergencyContactViewModel by activityViewModels<EmergencyContactsViewModel>()

    var resultLauncher: ActivityResultLauncher<Void?>? = null
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
            setMenuVisibility(false) // todo: investigate this, remove main options menu
            setHasOptionsMenu(false)
            updateEmergencyContact(it)

        } ?: run {
            // contact is null, create a new contact
            createNewEmergencyContact()

            // allow user to import contact
            setHasOptionsMenu(true)
            setUpPickContactLauncher(requireActivity().contentResolver)
        }

    }

    private fun updateEmergencyContact(contact: EmergencyContact) {

        binding.contactFullName.setText(contact.contactFullName)
        binding.contactPhoneNumber.setText(contact.contactPhoneNumber)

        binding.addOrUpdateContactButton.setOnClickListener {
            val contactFullName = binding.contactFullName.text.toString().trim()
            val contactPhoneNumber = binding.contactPhoneNumber.text.toString().trim()

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
            val contactFullName = binding.contactFullName.text.toString().trim()
            val contactPhoneNumber = binding.contactPhoneNumber.text.toString().trim()

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


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_add_contacts, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_import_contact) {
            val hasContactsPermissions = ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            )
            if (hasContactsPermissions == PERMISSION_GRANTED)
                resultLauncher?.launch(null)
            else {
                Toast.makeText(
                    requireContext(),
                    "You have to grant CONTACTS permissions first!",
                    Toast.LENGTH_LONG
                ).show()
                Utils.requestPermissionsFromSettings(requireContext())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpPickContactLauncher(contentResolver: ContentResolver) {
        resultLauncher = registerForActivityResult(ActivityResultContracts.PickContact()) { uri ->
            Log.d("TAG", uri?.userInfo.toString())

            uri?.let {
                val cursor: Cursor? = requireActivity().contentResolver.query(
                    uri, null,
                    null, null, null
                )

                cursor?.let { c ->
                    c.moveToFirst()

                    try {
                        val contactId =
                            c.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                        val contactDisplayName =
                            c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                        val hasPhoneNumber =
                            c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER)).toInt() == 1
                        var contactNumber : String? = null

                        if (hasPhoneNumber) {
                            contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = $contactId",
                                null,
                                null
                            ) ?.let {
                                it.moveToFirst()

                                contactNumber =
                                    it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))

                                it.close()
                            } ?: run {
                                c.close()
                            }
                        }

                        binding.contactFullName.setText(contactDisplayName)
                        binding.contactPhoneNumber.setText(contactNumber)

                    } catch (error: IllegalArgumentException) {
                        error.printStackTrace()
                        Toast.makeText(
                            requireContext(),
                            "An error occurred while trying to import contact",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                cursor?.close()
            }
        }
    }

}
