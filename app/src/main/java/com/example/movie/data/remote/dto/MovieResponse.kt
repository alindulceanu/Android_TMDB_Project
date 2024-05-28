package com.example.movie.data.remote.dto

import com.example.movie.database.model.GenreEntity
import com.example.movie.database.model.MovieEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieResponse(
    val page : Int,
    val results : List<MovieEntity>,
    @SerialName("total_pages") val totalPages : Int,
    @SerialName("total_results")val totalResults : Int
)

@Serializable
data class MovieResponseGenres(
    val genres: List<GenreEntity>
)