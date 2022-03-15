package org.xhanka.ndm_project.ui.weather.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import org.xhanka.ndm_project.R
import org.xhanka.ndm_project.data.models.weather.DailyForecast

class WeatherForeCastAdapter : ListAdapter<DailyForecast, WeatherForeCastAdapter.WeatherForeCastVH>(
    FORECAST_COMPARATOR
){

    class WeatherForeCastVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var townName: TextView = itemView.findViewById(R.id.townName)
        var weatherIcon: ImageView = itemView.findViewById(R.id.weatherIcon)
        var weatherTemperature: TextView = itemView.findViewById(R.id.weatherTemperature)
        var weatherDescription: TextView = itemView.findViewById(R.id.weatherDescription)
    }

    companion object {
        val FORECAST_COMPARATOR = object : DiffUtil.ItemCallback<DailyForecast>() {
            override fun areItemsTheSame(oldItem: DailyForecast, newItem: DailyForecast): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DailyForecast,
                newItem: DailyForecast
            ): Boolean {
                return oldItem.timeStamp == newItem.timeStamp
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherForeCastVH {
        return WeatherForeCastVH(LayoutInflater.from(parent.context).inflate(
            R.layout.forecast_item, parent, false
        ))
    }

    override fun onBindViewHolder(holder: WeatherForeCastVH, position: Int) {
        val forecast = getItem(position)

        holder.weatherIcon.load(forecast.weather[0].getIconUrl())
        holder.townName.text = forecast.timeStampText
        holder.weatherDescription.text = forecast.weather[0].description
        holder.weatherTemperature.text = forecast.weatherDetails.tempMax.toInt().toString()
    }
}