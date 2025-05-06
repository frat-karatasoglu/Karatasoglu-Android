package com.example.myapplication

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Badge
import com.example.myapplication.viewmodel.MovieViewModel
import com.example.myapplication.viewmodel.MovieViewModelFactory
import com.example.myapplication.repository.MovieRepository
import com.example.myapplication.datastore.SettingsManager
import com.example.myapplication.network.RetrofitInstance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp

@Composable
fun BottomNavigationBar(navController: NavController, movieViewModel: MovieViewModel) {
    val items = listOf(Screen.Movies, Screen.Favorites, Screen.Settings , Screen.Profile)
    val showNew = movieViewModel.showNew.collectAsState().value

    BottomNavigation(backgroundColor = Color(0xFF6200EE)) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    if (screen is Screen.Settings && showNew) {
                        BadgedBox(badge = {
                            Text("NEW", color = Color.White, fontSize = 10.sp)
                        }) {
                            Icon(imageVector = screen.icon!!, contentDescription = screen.title)
                        }
                    } else {
                        Icon(imageVector = screen.icon!!, contentDescription = screen.title)
                    }
                },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                selectedContentColor = Color.White,
                unselectedContentColor = Color.LightGray,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
