package com.example.myapplication.repository

import com.example.myapplication.room.FavoriteMovie
import com.example.myapplication.room.FavoriteMovieDao
import kotlinx.coroutines.flow.Flow

class FavoriteMovieRepository(private val dao: FavoriteMovieDao) {

    val allFavorites: Flow<List<FavoriteMovie>> = dao.getAllFavorites() // 🔁 UYUMLU İSİM

    suspend fun addToFavorites(movie: FavoriteMovie) {
        dao.insertFavorite(movie) // ✅ doğru fonksiyon ismi
    }

    suspend fun removeFromFavorites(movie: FavoriteMovie) {
        dao.deleteFavorite(movie) // ✅ doğru fonksiyon ismi
    }

    suspend fun isFavorite(movieId: String): Boolean {
        return dao.isFavorite(movieId) // ✅ zaten Boolean döner
    }
}
