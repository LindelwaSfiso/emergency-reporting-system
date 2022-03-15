package org.xhanka.ndm_project.data.models.weather

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Weather(
    @SerializedName("id")
    val id: Int,

    @SerializedName("main")
    val main: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("icon")
    val icon: String
): Parcelable {
    fun getIconUrl(): String {
        return "https://openweathermap.org/img/w/${icon}.png"
    }
}


//{
//    "id":800,
//    "main":"Clear",
//    "description":"clear sky",
//    "icon":"01d"
//}