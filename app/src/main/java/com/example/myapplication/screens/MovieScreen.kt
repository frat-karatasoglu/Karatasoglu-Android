package com.example.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.viewmodel.MovieUiState
import com.example.myapplication.viewmodel.MovieViewModel
import com.example.myapplication.viewmodel.MovieViewModelFactory
import com.example.myapplication.network.RetrofitInstance
import com.example.myapplication.repository.MovieRepository
import com.example.myapplication.ui.components.MovieItem
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun MovieScreen(navController: NavController) {
    val context = LocalContext.current

    // ViewModel'i oluştur
    val repository = remember { MovieRepository(RetrofitInstance.api) }
    val viewModel: MovieViewModel = viewModel(factory = MovieViewModelFactory(repository))

    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by remember { viewModel.searchQuery }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Популярные фильмы",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Arama Çubuğu
        TextField(
            value = searchQuery,
            onValueChange = { viewModel.onSearchQueryChange(it) },
            placeholder = { Text("Поиск фильма...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        when (uiState) {
            is MovieUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is MovieUiState.Error -> {
                val message = (uiState as MovieUiState.Error).message
                Text(text = "Hata: $message", color = MaterialTheme.colors.error)
            }

            is MovieUiState.Success -> {
                val filteredMovies = viewModel.getFilteredMovies()

                LazyColumn {
                    items(filteredMovies) { movie ->
                        MovieItem(movie) {
                            val rating = movie.rating?.kp ?: "Bilinmiyor"
                            val posterUrl = movie.poster?.url ?: ""
                            val description = movie.description ?: "Açıklama bulunmuyor."

                            navController.navigate(
                                "movie_detail/${URLEncoder.encode(movie.name, StandardCharsets.UTF_8.toString())}" +
                                        "/${movie.year ?: "Bilinmiyor"}" +
                                        "/$rating" +
                                        "/${URLEncoder.encode(posterUrl, StandardCharsets.UTF_8.toString())}" +
                                        "/${URLEncoder.encode(description, StandardCharsets.UTF_8.toString())}"
                            )
                        }
                    }
                }
            }
        }
    }
}
