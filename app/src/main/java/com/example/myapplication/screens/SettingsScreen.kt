package com.example.myapplication.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.datastore.SettingsManager
import com.example.myapplication.network.RetrofitInstance
import com.example.myapplication.repository.MovieRepository
import com.example.myapplication.viewmodel.MovieViewModel
import com.example.myapplication.viewmodel.MovieViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    context: Context,
    navController: NavController
) {
    val coroutineScope = rememberCoroutineScope()
    val settingsManager = remember { SettingsManager(context) }


    val repository = remember { MovieRepository(RetrofitInstance.api) }
    val viewModel: MovieViewModel = viewModel(factory = MovieViewModelFactory(repository, context))


    var selectedGenre by remember { mutableStateOf("Все") }
    var minRating by remember { mutableStateOf(0f) }
    var filmName by remember { mutableStateOf("") }


    LaunchedEffect(Unit) {
        selectedGenre = settingsManager.genreFlow.first()
        minRating = settingsManager.ratingFlow.first()
        filmName = settingsManager.nameFlow.first()
    }

    val genreOptions = listOf("Все", "Боевик", "Комедия", "Драма", "Фантастика")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Настройки фильтра", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))


        var expanded by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(selectedGenre)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                genreOptions.forEach { genre ->
                    DropdownMenuItem(onClick = {
                        selectedGenre = genre
                        expanded = false
                    }) {
                        Text(genre)
                    }
                }
            }
        }


        Text("Минимальная оценка: ${minRating.toInt()}", modifier = Modifier.padding(bottom = 8.dp))
        Slider(
            value = minRating,
            onValueChange = { minRating = it },
            valueRange = 0f..10f,
            steps = 9,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )


        OutlinedTextField(
            value = filmName,
            onValueChange = { filmName = it },
            label = { Text("Название фильма") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        Button(onClick = {
            coroutineScope.launch {
                settingsManager.saveFilters(
                    genre = selectedGenre,
                    rating = minRating,
                    name = filmName
                )

                viewModel.fetchFilteredMovies()
                navController.popBackStack()
            }
        }) {
            Text("Применить")
        }


    }
}
