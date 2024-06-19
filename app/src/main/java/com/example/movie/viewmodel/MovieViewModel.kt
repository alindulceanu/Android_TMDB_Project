package com.example.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository,
    private var savedStateHandle: SavedStateHandle
): ViewModel() {

    init {
        val service: MovieService = MovieService.create()

        viewModelScope.launch {
            try {
                val movies = service.getMovies().results
                insertMoviesFromRequest(movies)
            } catch (e: IOException) {
                // Handle network error
                _state.update { it.copy(message = "Network error: ${e.message}") }
            } catch (e: Exception) {
                // Handle other errors
                _state.update { it.copy(message = "Unexpected error: ${e.message}") }
            }
        }
    }

    private val _filterType = savedStateHandle.getLiveData("filterType", FilterType.POPULARITY)
    private val filterType: Flow<FilterType> = _filterType.asFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _movies = filterType
        .flatMapLatest { filterType ->
            repository.sortMovies(filterType)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList<MovieEntity>())

    private val _state = MutableStateFlow(MovieState(isLoaded = true))

    val state = combine(_state, filterType, _movies) { state, filterType, movies ->
        state.copy(
            movies = movies,
            filterType = filterType
        )
    }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MovieState())

    fun onEvent(event: MovieEvents){
        when (event){
            is MovieEvents.FilterMovies -> {
                savedStateHandle["filterType"] = event.filterType
                _filterType.value = event.filterType
            }
            is MovieEvents.FavoriteMovie -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.favoriteMovie(event.idDatabase)
                }
            }
        }
    }

    private suspend fun insertMoviesFromRequest (movies: List<MovieEntity>) {
        movies.forEach { movie ->
            repository.insertMovie(movie)
        }
    }

    suspend fun disconnectUser(username: String) {
        repository.disconnectUser(username)
    }
}

