package org.xhanka.ndm_project.data.api

import org.xhanka.ndm_project.data.models.weather.ForecastResponse
import org.xhanka.ndm_project.data.models.weather.WeatherResponse
import org.xhanka.ndm_project.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Url

interface WeatherApiService {
    @GET(Constants.WEATHER_FOR_ESWATINI)
    suspend fun getWeatherForEswatiniMajorTowns(): WeatherResponse

    @GET
    suspend fun getForecastForTown(@Url url: String): ForecastResponse
}