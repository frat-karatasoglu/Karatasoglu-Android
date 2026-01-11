package com.example.myapplication.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

// DataStore tanımı
val Context.dataStore by preferencesDataStore(name = "settings")

// ✅ Filtreleri bir arada taşıyan veri sınıfı
data class FilterSettings(
    val genre: String,
    val rating: Float,
    val name: String
)

class SettingsManager(private val context: Context) {

    private val GENRE_KEY = stringPreferencesKey("genre")
    private val RATING_KEY = floatPreferencesKey("rating")
    private val NAME_KEY = stringPreferencesKey("film_name")

    // ✅ Filtreleri kaydet
    suspend fun saveFilters(genre: String, rating: Float, name: String) {
        context.dataStore.edit { preferences ->
            preferences[GENRE_KEY] = genre
            preferences[RATING_KEY] = rating
            preferences[NAME_KEY] = name
        }
    }
    // SettingsManager.kt
    suspend fun resetFilters() {
        // Varsayılan değerlere sıfırlıyoruz
        saveFilters("Все", 0f, "") // Varsayılan tür, sıfır puan ve boş film adı
    }

    // ✅ Filtre değer akışları
    val genreFlow: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[GENRE_KEY] ?: "Все" }

    val ratingFlow: Flow<Float> = context.dataStore.data
        .map { preferences -> preferences[RATING_KEY] ?: 0f }

    val nameFlow: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[NAME_KEY] ?: "" }
}

// ✅ Tüm filtreleri birleştirip tek Flow hâline getiren fonksiyon
fun SettingsManager.getFilters(): Flow<FilterSettings> {
    return combine(genreFlow, ratingFlow, nameFlow) { genre, rating, name ->
        FilterSettings(genre, rating, name)
    }
}
