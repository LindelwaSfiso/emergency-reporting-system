package org.xhanka.ndm_project.data.models.weather

import com.google.gson.annotations.SerializedName
import org.xhanka.ndm_project.data.models.weather.DailyForecast

data class ForecastResponse(
    @SerializedName("cod")
    val statusCode: String,

    @SerializedName("message")
    val statusMessage: String,

    @SerializedName("cnt")
    val cnt: Int,

    @SerializedName("list")
    val dailyWeather: List<DailyForecast>
)

