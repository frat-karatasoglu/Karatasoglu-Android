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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.room.AppDatabase
import com.example.myapplication.repository.FavoriteMovieRepository
import com.example.myapplication.viewmodel.FavoriteViewModel
import com.example.myapplication.viewmodel.FavoriteViewModelFactory
import com.example.myapplication.models.Movie
import com.example.myapplication.models.Rating
import com.example.myapplication.models.Poster
import com.example.myapplication.room.FavoriteMovie
import com.example.myapplication.ui.components.MovieItem
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun FavoriteScreen(navController: NavController) {
    val context = LocalContext.current
    val dao = AppDatabase.getDatabase(context).favoriteMovieDao()
    val repository = FavoriteMovieRepository(dao)
    val viewModel: FavoriteViewModel = viewModel(factory = FavoriteViewModelFactory(repository))

    val favoriteMovies by viewModel.favoriteMovies.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Избранные фильмы", style = MaterialTheme.typography.h5)

        Spacer(modifier = Modifier.height(16.dp))

        if (favoriteMovies.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Нет избранных фильмов.")
            }
        } else {
            LazyColumn {
                items(favoriteMovies) { movie ->
                    val movieModel = Movie(
                        name = movie.name,
                        year = movie.year,
                        rating = Rating(kp = movie.rating, imdb = null), // ✅ imdb = null ekledim
                        poster = Poster(url = movie.posterUrl),
                        description = movie.description
                    )
                    MovieItem(movie = movieModel) {
                        navController.navigate(
                            "movie_detail/${URLEncoder.encode(movieModel.name, StandardCharsets.UTF_8.toString())}" +
                                    "/${movieModel.year ?: "Bilinmiyor"}" +
                                    "/${movieModel.rating?.kp ?: "Bilinmiyor"}" +
                                    "/${URLEncoder.encode(movieModel.poster?.url ?: "", StandardCharsets.UTF_8.toString())}" +
                                    "/${URLEncoder.encode(movieModel.description ?: "Açıklama bulunamadı", StandardCharsets.UTF_8.toString())}"
                        )
                    }
                }
            }
        }
    }
}
