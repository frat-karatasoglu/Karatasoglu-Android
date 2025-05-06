package com.example.myapplication.repository

import com.example.myapplication.models.MovieResponse
import com.example.myapplication.network.KinopoiskApi

class MovieRepository(private val api: KinopoiskApi) {
    suspend fun getMovies(): MovieResponse {
        return api.getMovies()
    }
}


