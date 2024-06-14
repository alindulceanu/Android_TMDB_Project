package com.example.movie.viewmodel.events



sealed interface MovieEvents {
    data class FavoriteMovie(val id: Int): MovieEvents
    data class FilterMovies(val filterType: FilterType): MovieEvents
}