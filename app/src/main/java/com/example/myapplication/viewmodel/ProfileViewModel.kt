// ProfileViewModel.kt
package com.example.myapplication.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.datastore.UserProfileManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class UserProfile(
    val name: String = "",
    val jobTitle: String = "",
    val resumeUrl: String = "",
    val avatarUri: String = ""
)

class ProfileViewModel(context: Context) : ViewModel() {

    private val profileManager = UserProfileManager(context)

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            val name = profileManager.getName()
            val job = profileManager.getJobTitle()
            val resume = profileManager.getResumeLink()
            val avatar = profileManager.getAvatarUri()
            _userProfile.value = UserProfile(name, job, resume, avatar)
        }
    }

    fun saveProfile(name: String, jobTitle: String, resumeUrl: String, avatarUri: String) {
        viewModelScope.launch {
            profileManager.saveName(name)
            profileManager.saveJobTitle(jobTitle)
            profileManager.saveResumeLink(resumeUrl)
            profileManager.saveAvatarUri(avatarUri)


            _userProfile.value = UserProfile(name, jobTitle, resumeUrl, avatarUri)
        }
    }


    fun resetProfile() {
        viewModelScope.launch {
            profileManager.clearProfile()
            _userProfile.value = UserProfile()
        }
    }

}