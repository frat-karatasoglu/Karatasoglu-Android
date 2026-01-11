package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.repository.MovieRepository
import android.content.Context
import com.example.myapplication.datastore.SettingsManager

class MovieViewModelFactory(
    private val repository: MovieRepository,
    private val context: Context // context parametresini burada geçiyoruz
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val settingsManager = SettingsManager(context) // SettingsManager'ı context ile oluşturuyoruz
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            return MovieViewModel(repository, settingsManager) as T // MovieViewModel oluşturuluyor
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

