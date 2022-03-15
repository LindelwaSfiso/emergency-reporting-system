package org.xhanka.ndm_project.ui.weather.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import org.xhanka.ndm_project.R
import org.xhanka.ndm_project.data.models.weather.Town
import org.xhanka.ndm_project.ui.weather.WeatherFragmentDirections
import java.util.*


/**
 * Adapter for displaying weather details for major towns
 *
 * User has option of selecting one major town, this will be shown in card on top
 */


class WeatherAdapter(
    private val navController: NavController
) : ListAdapter<Town, WeatherAdapter.WeatherVH>(TOWN_COMPARATOR) {

    class WeatherVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var townItemContainer: View = itemView.findViewById(R.id.townItemContainer)
        var townName: TextView = itemView.findViewById(R.id.townName)
        var townTemperature: TextView = itemView.findViewById(R.id.townTemperature)
        var weatherIcon: ImageView = itemView.findViewById(R.id.weatherIcon)
        var weatherDescription: TextView? = itemView.findViewById(R.id.weatherDescription)
    }

    companion object {
        val TOWN_COMPARATOR = object : DiffUtil.ItemCallback<Town>() {
            override fun areItemsTheSame(oldItem: Town, newItem: Town): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Town, newItem: Town): Boolean {
                return oldItem.townId == newItem.townId
            }

        }

        const val SELECTED_TOWN = 100
        const val MAJOR_TOWN = 200
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherVH {
        return if (viewType == MAJOR_TOWN)
            WeatherVH(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.weather_major_town_item, parent, false
                )
            )
        else
            WeatherVH(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.weather_selected_town_item, parent, false
                )
            )
    }

    override fun onBindViewHolder(holder: WeatherVH, position: Int) {
        val town = getItem(position)

        holder.weatherIcon.load(town.weather[0].getIconUrl())
        holder.townName.text = town.townName

        holder.townTemperature.text = town.weatherDetails.temp.toInt().toString()

        if (getItemViewType(position) == SELECTED_TOWN) {
            holder.weatherDescription?.text = town.weather[0].description.replaceFirstChar {
                it.titlecase(Locale.getDefault())
            }
        }

        holder.townItemContainer.setOnClickListener {
            val action = WeatherFragmentDirections.actionNavigationWeatherForEswatiniToNavigationWeatherForecast(
                town.townName
            )

            navController.navigate(action)
        }

    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0)
            return SELECTED_TOWN
        return MAJOR_TOWN
    }
}