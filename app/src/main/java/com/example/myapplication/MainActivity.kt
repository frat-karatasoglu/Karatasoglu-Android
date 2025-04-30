package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.repository.MovieRepository
import com.example.myapplication.network.RetrofitInstance
import com.example.myapplication.viewmodel.MovieViewModel
import com.example.myapplication.viewmodel.MovieViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val movieViewModel: MovieViewModel = viewModel(
        factory = MovieViewModelFactory(
            MovieRepository(RetrofitInstance.api),
            context
        )
    )

    Scaffold(
        bottomBar = { BottomNavigationBar(navController, movieViewModel) }
    ) { paddingValues ->
        Box(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
        ) {
            NavGraph(navController = navController, movieViewModel = movieViewModel)
        }
    }
}

