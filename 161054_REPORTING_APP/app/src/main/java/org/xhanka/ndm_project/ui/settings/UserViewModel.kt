package org.xhanka.ndm_project.ui.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.xhanka.ndm_project.data.database.MainDataBase
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(dataBase: MainDataBase): ViewModel() {
    val user = dataBase.userProfileDao().getUserProfile()

    // consider updating user here
}