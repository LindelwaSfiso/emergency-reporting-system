package org.xhanka.ndm_project.data.models.weather

import com.google.gson.annotations.SerializedName
import org.xhanka.ndm_project.data.models.weather.Town

class WeatherResponse(
    @SerializedName("cnt")
    val cnt: Int,

    @SerializedName("list")
    val towns: List<Town>
)