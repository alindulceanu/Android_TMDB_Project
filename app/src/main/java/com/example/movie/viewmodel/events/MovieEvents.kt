package com.example.movie.viewmodel.events



sealed interface MovieEvents {
    data class FilterMovies(val filterType: FilterType): MovieEvents
}