package com.example.myapplication

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.room.AppDatabase
import com.example.myapplication.repository.FavoriteMovieRepository
import com.example.myapplication.screens.*
import com.example.myapplication.viewmodel.*

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector? = null) {
    object Movies : Screen("movies", "Movies", Icons.Default.Movie)
    object MovieDetail : Screen("movie_detail/{title}/{year}/{rating}/{posterUrl}/{description}", "Movie Detail")
    object Settings : Screen("settings", "Фильтры", Icons.Default.Settings)
    object Favorites : Screen("favorites", "Избранное", Icons.Default.Person)
    object EditProfile : Screen("edit_profile", "Редактировать профиль")
    object Profile : Screen("profile", "Профиль", Icons.Default.Person)
}

@Composable
fun NavGraph(
    navController: NavHostController,
    movieViewModel: MovieViewModel
) {
    val context = LocalContext.current
    val profileViewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(context)) // 👈 Tek bir kez oluştur

    NavHost(navController = navController, startDestination = Screen.Movies.route) {

        composable(Screen.Movies.route) {
            MovieScreen(navController, movieViewModel)
        }

        composable(Screen.Favorites.route) {
            FavoriteScreen(navController)
        }

        composable(Screen.Settings.route) {
            SettingsScreen(context = context, navController = navController, movieViewModel = movieViewModel)
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController, profileViewModel = profileViewModel) // 👈 Aynı instance
        }

        composable(Screen.EditProfile.route) {
            EditProfileScreen(navController = navController, profileViewModel = profileViewModel) // 👈 Aynı instance
        }

        composable(Screen.MovieDetail.route) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: "Unknown"
            val year = backStackEntry.arguments?.getString("year") ?: "Unknown"
            val rating = backStackEntry.arguments?.getString("rating") ?: "Unknown"
            val posterUrl = backStackEntry.arguments?.getString("posterUrl") ?: ""
            val description = backStackEntry.arguments?.getString("description") ?: "Açıklama bulunamadı"

            val dao = AppDatabase.getDatabase(context).favoriteMovieDao()
            val repository = FavoriteMovieRepository(dao)
            val favoriteViewModel: FavoriteViewModel = viewModel(factory = FavoriteViewModelFactory(repository))

            MovieDetailScreen(title, year, rating, posterUrl, description, favoriteViewModel)
        }
    }
}


