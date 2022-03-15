package org.xhanka.ndm_project.data.models.weather

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Town(
    @SerializedName("name")
    val townName: String,

    @SerializedName("id")
    val townId: Int,

    @SerializedName("dt")
    val timeStamp: Long,

    @SerializedName("coord")
    val townCoordinates: TownCoordinate,

    @SerializedName("wind")
    val wind: Wind,

    @SerializedName("main")
    val weatherDetails: WeatherDetails,

    @SerializedName("weather")
    val weather: List<Weather>
): Parcelable

//    This was omitted, since we care about Eswatini only
//    @SerializedName("sys")
//    val country: Country



