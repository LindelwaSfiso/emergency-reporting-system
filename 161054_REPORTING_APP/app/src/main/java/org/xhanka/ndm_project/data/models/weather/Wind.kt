package org.xhanka.ndm_project.data.models.weather

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Wind(
    @SerializedName("speed")
    val speed: Double,

    @SerializedName("deg")
    val deg: Int,

    @SerializedName("gust")
    val gust: Double
): Parcelable

//    "speed":3.05,
//    "deg":60,
//    "gust":2.25
