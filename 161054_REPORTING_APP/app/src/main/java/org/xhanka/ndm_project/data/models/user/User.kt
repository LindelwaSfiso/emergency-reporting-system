package org.xhanka.ndm_project.data.models.user

import android.content.Context
import androidx.preference.PreferenceManager
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.encoders.annotations.Encodable
import org.xhanka.ndm_project.ui.settings.UserProfileFragment

@IgnoreExtraProperties
@Entity(tableName = "USER_PROFILE")
data class User(
    val uid: String = "",
    var fullName: String = "",
    var email: String = "",
    var ID: String = "",
    var phoneNumber: String = "",
    @PrimaryKey(autoGenerate = true)
    var _id: Int = 0
) {

    @Encodable.Ignore
    fun toHash(): Map<String, String> {
        return mapOf(
            "uid" to uid,
            "fullName" to fullName,
            "email" to email,
            "ID" to ID,
            "phoneNumber" to phoneNumber,
        )
    }

    fun saveToPreferences(context: Context) {
        val manager = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = manager.edit()
        editor.putString(UserProfileFragment.NAME_KEY, fullName)
        editor.putString(UserProfileFragment.ID_KEY, ID)
        editor.putString(UserProfileFragment.PHONE_NUMBER_KEY, phoneNumber)
        editor.putString(UserProfileFragment.EMAIL_KEY, email)
        editor.apply()
    }
}
