package org.xhanka.ndm_project.data.models.weather

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherDetails(
    @SerializedName("temp")
    val temp: Double,

    @SerializedName("feels_like")
    val feelsLike: Double,

    @SerializedName("temp_min")
    val tempMin: Double,

    @SerializedName("temp_max")
    val tempMax: Double,

    @SerializedName("pressure")
    val pressure: Double,

    @SerializedName("sea_level")
    val seaLevel: Double,

    @SerializedName("grnd_level")
    val groundLevel: Int,

    @SerializedName("humidity")
    val humidity: Double

): Parcelable

//"main":{
//    "temp":27.25,
//    "feels_like":26.89,
//    "temp_min":27.25,
//    "temp_max":27.25,
//    "pressure":1014,
//    "sea_level":1014,
//    "grnd_level":981,
//    "humidity":37
//}
