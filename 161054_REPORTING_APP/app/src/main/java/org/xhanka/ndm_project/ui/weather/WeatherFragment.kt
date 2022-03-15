package org.xhanka.ndm_project.ui.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.xhanka.ndm_project.data.utils.Resource
import org.xhanka.ndm_project.databinding.FragmentWeatherBinding
import org.xhanka.ndm_project.ui.weather.adapter.WeatherAdapter
import org.xhanka.ndm_project.data.utils.HandleNetworkLoading

@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private val weatherViewModel: WeatherViewModel by activityViewModels()
    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: WeatherAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = WeatherAdapter(findNavController())
        val recyclerView = binding.recyclerView

        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.addItemDecoration(DividerItemDecoration(
            view.context, DividerItemDecoration.VERTICAL
        ))

        recyclerView.adapter = adapter

        val handleNetworkLoading = HandleNetworkLoading(
            binding.progressBar,
            binding.recyclerView,
            binding.statusContainer.statusContainer
        )

        weatherViewModel.weather.observe(this, {
            when (it){
                is Resource.Loading -> handleNetworkLoading.showLoading()

                is Resource.Failure -> handleNetworkLoading.showError()

                is Resource.Success -> {
                    val towns = it.value.towns
                    adapter.submitList(towns)
                    handleNetworkLoading.hideLoading()
                }
            }
        })

        binding.statusContainer.retryButton.setOnClickListener {
            weatherViewModel.getWeather()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}