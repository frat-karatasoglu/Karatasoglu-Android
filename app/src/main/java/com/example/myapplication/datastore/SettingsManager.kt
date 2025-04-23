package com.example.myapplication.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Расширение для получения DataStore из Context
val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsManager(private val context: Context) {

    // Ключи для хранения значений
    private val GENRE_KEY = stringPreferencesKey("genre")
    private val RATING_KEY = floatPreferencesKey("rating")
    private val NAME_KEY = stringPreferencesKey("film_name")

    // Сохранение фильтров
    suspend fun saveFilters(genre: String, rating: Float, name: String) {
        context.dataStore.edit { preferences ->
            preferences[GENRE_KEY] = genre
            preferences[RATING_KEY] = rating
            preferences[NAME_KEY] = name
        }
    }

    // Загрузка жанра
    val genreFlow: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[GENRE_KEY] ?: "Все" }

    // Загрузка оценки
    val ratingFlow: Flow<Float> = context.dataStore.data
        .map { preferences -> preferences[RATING_KEY] ?: 0f }

    // Загрузка названия фильма
    val nameFlow: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[NAME_KEY] ?: "" }
}
