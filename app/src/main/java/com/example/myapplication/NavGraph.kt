package com.example.myapplication

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import com.example.myapplication.screens.*

sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {
    object Movies : Screen("movies", "Movies", Icons.Default.Movie)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
    object Login : Screen("login", "Giriş Yap")
    object MovieDetail : Screen("movie_detail/{title}/{year}/{rating}/{posterUrl}/{description}", "Movie Detail")
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Movies.route) {
        composable(Screen.Movies.route) {
            MovieScreen(navController)
        }

        composable(Screen.Login.route) {
            LoginScreen(navController)
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }

        composable(Screen.MovieDetail.route) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: "Unknown"
            val year = backStackEntry.arguments?.getString("year") ?: "Unknown"
            val rating = backStackEntry.arguments?.getString("rating") ?: "Unknown"
            val posterUrl = backStackEntry.arguments?.getString("posterUrl") ?: ""
            val description = backStackEntry.arguments?.getString("description") ?: "Açıklama bulunamadı"

            MovieDetailScreen(title, year, rating, posterUrl, description)
        }
    }
}
