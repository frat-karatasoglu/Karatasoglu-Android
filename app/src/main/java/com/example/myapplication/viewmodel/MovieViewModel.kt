package com.example.myapplication.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.models.Movie
import com.example.myapplication.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class MovieUiState {
    object Loading : MovieUiState()
    data class Success(val movies: List<Movie>) : MovieUiState()
    data class Error(val message: String) : MovieUiState()
}

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieUiState>(MovieUiState.Loading)
    val uiState: StateFlow<MovieUiState> = _uiState

    var searchQuery = mutableStateOf("")
        private set

    init {
        fetchMovies()
    }

    fun fetchMovies(page: Int = 1, limit: Int = 10) {
        _uiState.value = MovieUiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.getMovies(page, limit)
                _uiState.value = MovieUiState.Success(response.docs)
            } catch (e: Exception) {
                _uiState.value = MovieUiState.Error("Bir hata oluştu: ${e.message}")
            }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        searchQuery.value = newQuery
    }

    fun getFilteredMovies(): List<Movie> {
        val currentState = _uiState.value
        return if (currentState is MovieUiState.Success) {
            currentState.movies.filter {
                it.name.contains(searchQuery.value, ignoreCase = true)
            }
        } else emptyList()
    }
}
