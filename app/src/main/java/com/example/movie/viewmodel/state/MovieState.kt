package com.example.movie.viewmodel.state

import com.example.movie.database.model.MovieEntity
import com.example.movie.viewmodel.events.FilterType

data class MovieState(
    var isLoaded: Boolean = false,
    var movies: List<MovieEntity> = emptyList(),
    var filterType: FilterType = FilterType.POPULARITY,
    var message: String? = null
)
