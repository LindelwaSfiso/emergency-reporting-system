package org.xhanka.ndm_project.ui.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.xhanka.ndm_project.data.models.weather.ForecastResponse
import org.xhanka.ndm_project.data.utils.Resource
import org.xhanka.ndm_project.databinding.FragmentWeatherForecastBinding
import org.xhanka.ndm_project.ui.weather.adapter.WeatherForeCastAdapter
import org.xhanka.ndm_project.utils.Constants
import org.xhanka.ndm_project.data.utils.HandleNetworkLoading

class WeatherForecastFragment : Fragment() {

    private val weatherViewModel: WeatherViewModel by activityViewModels()
    private lateinit var binding: FragmentWeatherForecastBinding
    private lateinit var handleNetworkLoading: HandleNetworkLoading
    private val args by navArgs<WeatherForecastFragmentArgs>()

    private val adapter = WeatherForeCastAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherForecastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleNetworkLoading =  HandleNetworkLoading(
            binding.progressBar,
            binding.recyclerView,
            binding.statusContainer.statusContainer
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(
            view.context
        )

        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL)
        )

        weatherViewModel.townForeCast.observe(this){
            handleForeCastResponse(it)
        }

        binding.recyclerView.adapter = adapter

        weatherViewModel.getWeatherForecastForTown(
            String.format(Constants.FORECAST_FOR_TOWN, args.townName)
        )
    }

    private fun handleForeCastResponse(it: Resource<ForecastResponse>) {
        when(it) {
            is Resource.Loading -> handleNetworkLoading.showLoading()
            is Resource.Failure -> handleNetworkLoading.showError()

            is Resource.Success -> {
                adapter.submitList(it.value.dailyWeather)
                handleNetworkLoading.hideLoading()

            }
        }
    }
}