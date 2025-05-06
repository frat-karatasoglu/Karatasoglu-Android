package com.example.myapplication.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey

private val Context.userProfileDataStore by preferencesDataStore(name = "user_profile")

class UserProfileManager(private val context: Context) {

    companion object {
        private val NAME = stringPreferencesKey("name")
        private val JOB_TITLE = stringPreferencesKey("job_title")
        private val RESUME_LINK = stringPreferencesKey("resume_link")
        private val AVATAR_URI = stringPreferencesKey("avatar_uri")
    }

    suspend fun saveName(name: String) {
        context.userProfileDataStore.edit { it[NAME] = name }
    }

    suspend fun saveJobTitle(title: String) {
        context.userProfileDataStore.edit { it[JOB_TITLE] = title }
    }

    suspend fun saveResumeLink(link: String) {
        context.userProfileDataStore.edit { it[RESUME_LINK] = link }
    }

    suspend fun saveAvatarUri(uri: String) {
        context.userProfileDataStore.edit { it[AVATAR_URI] = uri }
    }

    suspend fun getName(): String = context.userProfileDataStore.data.map { it[NAME] ?: "" }.first()
    suspend fun getJobTitle(): String = context.userProfileDataStore.data.map { it[JOB_TITLE] ?: "" }.first()
    suspend fun getResumeLink(): String = context.userProfileDataStore.data.map { it[RESUME_LINK] ?: "" }.first()
    suspend fun getAvatarUri(): String = context.userProfileDataStore.data.map { it[AVATAR_URI] ?: "" }.first()


    suspend fun clearProfile() {
        context.userProfileDataStore.edit { it.clear() }
    }
}
