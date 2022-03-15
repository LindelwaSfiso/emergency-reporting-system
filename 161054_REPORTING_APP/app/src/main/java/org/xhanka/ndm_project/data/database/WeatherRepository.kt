package org.xhanka.ndm_project.data.database

import org.xhanka.ndm_project.data.api.WeatherApiService
import org.xhanka.ndm_project.data.utils.SafeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(private val apiService: WeatherApiService):
    SafeApiCall {

    suspend fun getWeatherForEswatiniMajorTowns() = safeApiCall {
        apiService.getWeatherForEswatiniMajorTowns()
    }

    suspend fun getForecastForTown(url: String) = safeApiCall {
        apiService.getForecastForTown(url)
    }
}