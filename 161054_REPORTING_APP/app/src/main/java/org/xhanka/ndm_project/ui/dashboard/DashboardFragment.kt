package org.xhanka.ndm_project.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerAdapter
import org.xhanka.ndm_project.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private val dashboardViewModel by activityViewModels<DashboardViewModel>()
    private var _binding: FragmentDashboardBinding? = null
    //private lateinit var adapter: FirebaseRecyclerAdapter<RandomData, DashboardViewModel.ViewHolder>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        //adapter = dashboardViewModel.getRecyclerViewAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addData.setOnClickListener {
            val text = binding.editText.text.toString()
            dashboardViewModel.sentences(it.context, text)
        }

        binding.tapAndHold.setOnButtonClickListener(object: TapHoldUpButton.OnButtonClickListener {
            override fun onLongHoldStart(v: View?) {
                Log.d("TAG", "ON LONG HOLD START")
            }

            override fun onLongHoldEnd(v: View?) {
                Log.d("TAG", "ON LONG HOLD END")
            }

            override fun onClick(v: View?) {
                Log.d("TAG", "ON CLICK")
            }

        })

        /*binding.recyclerView.layoutManager = LinearLayoutManager(view.context)

        dashboardViewModel.randomData.observe(viewLifecycleOwner) {
            Log.d("TAG", it.toString())
        }

        binding.addData.setOnClickListener {
            dashboardViewModel.addData()
        }

        binding.removeData.setOnClickListener {
            dashboardViewModel.removeData()
        }

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(DividerItemDecoration(
            view.context, DividerItemDecoration.VERTICAL)
        )*/
    }

    override fun onStart() {
        super.onStart()
        //adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        //adapter.stopListening()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
