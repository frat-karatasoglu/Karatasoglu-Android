package com.example.myapplication.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    @SerialName("name") val name: String,
    @SerialName("year") val year: Int?,
    @SerialName("rating") val rating: Rating?,
    @SerialName("poster") val poster: Poster?,
    @SerialName("description") val description: String?
)

@Serializable
data class Rating(
    @SerialName("kp") val kp: Float?,
    @SerialName("imdb") val imdb: Float?
)

@Serializable
data class Poster(
    @SerialName("url") val url: String?
)

@Serializable
data class MovieResponse(
    @SerialName("docs") val docs: List<Movie>
)
