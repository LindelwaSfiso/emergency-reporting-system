package org.xhanka.ndm_project.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import org.xhanka.ndm_project.data.models.User
import org.xhanka.ndm_project.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dashboardViewModel =
            ViewModelProvider(this)[DashboardViewModel::class.java]

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val currentUser = Firebase.auth.currentUser!!

//        val dataBase = FirebaseDatabase.getInstance()
//        dataBase.setLogLevel(Logger.Level.DEBUG)

//        val users = dataBase.reference.child("USERS")
//
//        users.get().addOnCompleteListener {
//            if (it.isSuccessful) {
//
//               for (count in it.result.children) {
//                   Log.d("TAG", count.toString())
//               }
//                Log.d("TAG", it.result.toString())
//
//            } else {
//                Log.d("TAG", "NOT SUCCESSFUL")
//            }
//        }

//        val languages = dataBase.reference.child("Languages")
//        languages.push().child("name").setValue("Java")
//        languages.push().child("name").setValue("Kotlin")
//        languages.push().child("name").setValue("Swift")


//        languages.addValueEventListener(object: ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                snapshot.children.forEach {
//                    Log.d("TAG", it.value.toString())
//                }
//
//                dashboardViewModel.setTex(snapshot.toString())
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                dashboardViewModel.setTex(error.details.toString())
//            }
//
//        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}