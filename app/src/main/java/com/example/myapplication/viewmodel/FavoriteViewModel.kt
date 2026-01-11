package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.repository.FavoriteMovieRepository
import com.example.myapplication.room.FavoriteMovie
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: FavoriteMovieRepository) : ViewModel() {


    val favoriteMovies: StateFlow<List<FavoriteMovie>> = repository.allFavorites
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addFavorite(movie: FavoriteMovie) {
        viewModelScope.launch {
            repository.addToFavorites(movie)
        }
    }

    fun removeFavorite(movie: FavoriteMovie) {
        viewModelScope.launch {
            repository.removeFromFavorites(movie)
        }
    }

    suspend fun isFavorite(movieId: String): Boolean {
        return repository.isFavorite(movieId)
    }
}
