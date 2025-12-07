package com.example.myapplication.repository

import com.example.myapplication.models.MovieResponse
import com.example.myapplication.network.KinopoiskApi

class MovieRepository(private val api: KinopoiskApi) {
    suspend fun getMovies(page: Int = 1, limit: Int = 10): MovieResponse {
        return api.getMovies(page = page, limit = limit)
    }
}
