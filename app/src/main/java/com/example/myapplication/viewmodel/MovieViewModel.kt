package com.example.myapplication.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.datastore.SettingsManager
import com.example.myapplication.models.Movie
import com.example.myapplication.repository.MovieRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.myapplication.datastore.getFilters // ✅ Bunu ekle!



sealed class MovieUiState {
    object Loading : MovieUiState()
    data class Success(val movies: List<Movie>) : MovieUiState()
    data class Error(val message: String) : MovieUiState()
}

class MovieViewModel(
    private val repository: MovieRepository,
    private val settingsManager: SettingsManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieUiState>(MovieUiState.Loading)
    val uiState: StateFlow<MovieUiState> = _uiState

    var searchQuery = mutableStateOf("")
        private set

    private var currentGenre = "Все"
    private var currentRating = 0f
    private var currentName = ""

    // ✅ Badge için durum
    private val _showBadge = MutableStateFlow(false)
    val showBadge: StateFlow<Boolean> = _showBadge

    init {
        fetchFilteredMovies() // ✅ badge kontrolü başlat
    }

    fun onSearchQueryChange(newQuery: String) {
        searchQuery.value = newQuery
    }

    fun fetchFilteredMovies() {
        _uiState.value = MovieUiState.Loading

        viewModelScope.launch {
            try {
                currentGenre = settingsManager.genreFlow.first()
                currentRating = settingsManager.ratingFlow.first()
                currentName = settingsManager.nameFlow.first()

                println("🎯 Filtreler -> Tür: $currentGenre, Puan: $currentRating, İsim: $currentName")

                val response = repository.getMovies()
                println("🎯 Gelen Filmler: ${response.docs}")

                _uiState.value = MovieUiState.Success(response.docs)
            } catch (e: Exception) {
                _uiState.value = MovieUiState.Error("Ошибка: ${e.message}")
            }
        }
    }

    fun getFilteredMovies(): List<Movie> {
        val currentState = _uiState.value
        return if (currentState is MovieUiState.Success) {
            currentState.movies.filter { movie ->
                val matchesNameFromSettings = currentName.isEmpty() || movie.name.contains(currentName, ignoreCase = true)
                val matchesSearchQuery = searchQuery.value.isEmpty() || movie.name.contains(searchQuery.value, ignoreCase = true)
                val matchesRating = movie.rating?.kp ?: 0f >= currentRating
                val matchesGenre = currentGenre == "Все" ||
                        movie.genres?.any { it.name.contains(currentGenre, ignoreCase = true) } == true

                matchesNameFromSettings && matchesSearchQuery && matchesRating && matchesGenre
            }
        } else emptyList()
    }

    // MovieViewModel.kt'de showBadge'i güncelleyin
    private val _showNew = MutableStateFlow(false)
    val showNew: StateFlow<Boolean> = _showNew

    fun markFilterApplied() {
        _showNew.value = true
    }

    fun clearFilterMark() {
        _showNew.value = false
    }
}



