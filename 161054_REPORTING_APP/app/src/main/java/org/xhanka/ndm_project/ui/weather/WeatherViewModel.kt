package org.xhanka.ndm_project.ui.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.xhanka.ndm_project.data.database.WeatherRepository
import org.xhanka.ndm_project.data.models.weather.ForecastResponse
import org.xhanka.ndm_project.data.models.weather.WeatherResponse
import org.xhanka.ndm_project.data.utils.Resource
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: WeatherRepository): ViewModel() {

    private var _weather: MutableLiveData<Resource<WeatherResponse>> = MutableLiveData()
    val weather: LiveData<Resource<WeatherResponse>>
        get() = _weather

    private var _townForeCast: MutableLiveData<Resource<ForecastResponse>> = MutableLiveData()
    val townForeCast: LiveData<Resource<ForecastResponse>>
        get() = _townForeCast

    init {
        getWeather()
    }

    fun getWeather() = viewModelScope.launch {
        _weather.postValue(Resource.Loading)
        _weather.postValue(repository.getWeatherForEswatiniMajorTowns())
    }

    fun getWeatherForecastForTown(url: String) = viewModelScope.launch {
        _townForeCast.postValue(Resource.Loading)
        _townForeCast.postValue(repository.getForecastForTown(url))
    }
}


