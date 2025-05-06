package com.example.myapplication.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
data class FavoriteMovie(
    @PrimaryKey val id: String,
    val name: String,
    val year: Int?,
    val rating: Float?,
    val posterUrl: String?,
    val description: String?
)
