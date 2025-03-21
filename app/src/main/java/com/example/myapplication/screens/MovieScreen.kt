package com.example.myapplication.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.network.RetrofitInstance
import com.example.myapplication.models.Movie
import kotlinx.coroutines.launch
import com.example.myapplication.ui.components.MovieItem
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun MovieScreen(navController: NavController) {
    var movies by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var searchText by remember { mutableStateOf("") } // 🔹 Arama için metin durumu
    var filteredMovies by remember { mutableStateOf<List<Movie>>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitInstance.api.getMovies()
            movies = response.docs
            filteredMovies = response.docs // 🔹 İlk başta tüm filmleri göster
            Log.d("API_RESPONSE", response.toString())
        } catch (e: Exception) {
            Log.e("API_ERROR", "Hata: ${e.message}")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Popüler Filmler", fontSize = 24.sp, modifier = Modifier.padding(bottom = 8.dp))

        // 🔹 Arama Çubuğu
        TextField(
            value = searchText,
            onValueChange = { query ->
                searchText = query
                filteredMovies = if (query.isEmpty()) {
                    movies // Arama boşsa tüm filmleri göster
                } else {
                    movies.filter { it.name.contains(query, ignoreCase = true) }
                }
            },
            placeholder = { Text("Film ara...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        LazyColumn {
            items(filteredMovies) { movie ->
                MovieItem(movie) {
                    val rating = movie.rating?.kp ?: "Bilinmiyor"
                    val posterUrl = movie.poster?.url ?: ""
                    val description = movie.description ?: "Açıklama bulunmuyor."

                    navController.navigate(
                        "movie_detail/${URLEncoder.encode(movie.name, StandardCharsets.UTF_8.toString())}" +
                                "/${movie.year ?: "Bilinmiyor"}" +
                                "/${rating}" +
                                "/${URLEncoder.encode(posterUrl, StandardCharsets.UTF_8.toString())}" +
                                "/${URLEncoder.encode(description, StandardCharsets.UTF_8.toString())}"
                    )
                }
            }
        }
    }
}
