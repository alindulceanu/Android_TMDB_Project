package com.example.movie.viewmodel.state

import com.example.movie.database.model.MovieEntity
import com.example.movie.viewmodel.events.FilterType

data class MovieState(
    var movies: List<MovieEntity> = emptyList(),
    var filterType: FilterType = FilterType.POPULARITY,
    var isReadingInfo: Boolean = false,
    var movieInfo: MovieEntity? = null
)
