package com.example.myapplication.api

import com.example.myapplication.models.Movie
import retrofit2.http.GET
import retrofit2.http.Header

interface MovieApi {
    @GET("v1.4/movie?limit=10")
    suspend fun getMovies(
        @Header("X-API-KEY") apiKey: String = "CSMBD08-B5H4XZN-J27AVJ0-EQBKNPK"
    ): List<Movie>
}
