package org.xhanka.ndm_project.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.xhanka.ndm_project.data.database.MainDataBase
import org.xhanka.ndm_project.utils.Constants
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(dataBase: MainDataBase): ViewModel() {
    val user = dataBase.userProfileDao().getUserProfile()
    private val db = Firebase.firestore

    private var _version: MutableLiveData<String> = MutableLiveData()
    val version: LiveData<String> = _version

    // consider updating user here
    fun getDbVersion() = viewModelScope.launch (Dispatchers.IO) {
        try {
            val version = db.collection(Constants.DB_CONFIG_COLLECTION).document(
                Constants.DOCUMENT_SETTINGS
            ).get().await()
            val versionString = version.toObject<String>()
            _version.postValue(versionString!!)
        } catch (ignore: Exception) {
            // ignore
        }

    }
}