package org.xhanka.ndm_project.ui.dashboard

import android.R.attr.author
import com.google.firebase.database.Exclude


data class RandomData (
    val name: String = "",
    val surname : String = "",
    val randomNum: String = "1"
) {
    @Exclude
    fun toMap(): Map<String, Any> {
        val result: HashMap<String, Any> = HashMap()
        result["name"] = name
        result["surname"] = surname
        result["randomNum"] = randomNum
        return result
    }
}