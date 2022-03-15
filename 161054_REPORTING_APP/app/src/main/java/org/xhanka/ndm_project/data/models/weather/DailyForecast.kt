package org.xhanka.ndm_project.data.models.weather

import com.google.gson.annotations.SerializedName

data class DailyForecast(
    @SerializedName("dt")
    val timeStamp: Long,

    @SerializedName("dt_txt")
    val timeStampText: String,

    @SerializedName("main")
    val weatherDetails: WeatherDetails,

    @SerializedName("weather")
    val weather: List<Weather>,

    @SerializedName("wind")
    val wind: Wind
)

//"clouds":{
//    "all":5
//},
//"wind":{
//    "speed":3.05,
//    "deg":60,
//    "gust":2.25
//},
//"visibility":10000,
//"pop":0,
//"sys":{
//    "pod":"d"
//},
//"dt_txt":"2022-01-18 12:00:00"
