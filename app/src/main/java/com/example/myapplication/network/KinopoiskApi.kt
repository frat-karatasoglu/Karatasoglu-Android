package com.example.myapplication.network

import com.example.myapplication.models.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

const val API_KEY = "CSMBD08-B5H4XZN-J27AVJ0-EQBKNPK"

interface KinopoiskApi {
    @GET("v1.3/movie")
    suspend fun getMovies(
        @Header("X-API-KEY") apiKey: String = API_KEY,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): MovieResponse
}
