package com.example.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie.data.remote.MovieService
import com.example.movie.database.dao.MovieDao
import com.example.movie.database.model.GenreEntity
import com.example.movie.database.model.MovieEntity
import com.example.movie.viewmodel.events.FilterType
import com.example.movie.viewmodel.events.MovieEvents
import com.example.movie.viewmodel.repositories.GenreRepository
import com.example.movie.viewmodel.repositories.MovieRepository
import com.example.movie.viewmodel.state.MovieState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository
): ViewModel() {

    init {
        val service: MovieService = MovieService.create()

        viewModelScope.launch {
            insertMoviesFromRequest(
                service.getMovies().results
            )
        }
    }

    private val _filterType = MutableStateFlow(FilterType.POPULARITY)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _movies = _filterType
        .flatMapLatest { filterType ->
            repository.sortMovies(filterType)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList<MovieEntity>())

    private val _state = MutableStateFlow(MovieState())

    val state = combine(_state, _filterType, _movies) { state, filterType, movies ->
        state.copy(
            movies = movies,
            filterType = filterType
        )
    }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MovieState())

    fun onEvent(event: MovieEvents){
        when (event){
            is MovieEvents.FilterMovies -> {
                _filterType.value = event.filterType
            }
            is MovieEvents.FavoriteMovie -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.favoriteMovie(event.id)
                }
            }
        }
    }

    private suspend fun insertMoviesFromRequest (movies: List<MovieEntity>) {
        movies.forEach { movie ->
            repository.insertMovie(movie)
        }
    }
    private operator fun <T> StateFlow<T>.get(id: Int): MovieEntity {
        return this[id]
    }
}

