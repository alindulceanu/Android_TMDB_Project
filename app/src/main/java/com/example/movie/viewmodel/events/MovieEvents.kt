package com.example.movie.viewmodel.events

import com.example.movie.database.model.MovieEntity

sealed interface MovieEvents {
    object HideMovieInfo: MovieEvents
    data class ShowMovieInfo(val movie: MovieEntity) : MovieEvents
    data class FavoriteMovie(val id: Int): MovieEvents
    data class FilterMovies(val filterType: FilterType): MovieEvents
}