package com.example.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie.database.dao.MovieDao
import com.example.movie.database.model.GenreEntity
import com.example.movie.database.model.MovieEntity
import com.example.movie.viewmodel.events.FilterType
import com.example.movie.viewmodel.events.MovieEvents
import com.example.movie.viewmodel.repositories.GenreRepository
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
    private val dao: MovieDao,
    private val repository: GenreRepository
): ViewModel() {

    private val _filterType = MutableStateFlow(FilterType.POPULARITY)
    private val _genres = MutableLiveData<List<GenreEntity>>()
    val genres : LiveData<List<GenreEntity>> get() = _genres

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _movies = _filterType
        .flatMapLatest { filterType ->
            when(filterType){
                FilterType.POPULARITY -> dao.orderMoviesByPopularity()
                FilterType.FAVORITES -> dao.orderFavouriteMovies()
                FilterType.RATING -> dao.orderMoviesByRating()
                FilterType.RELEASE_DATE -> dao.orderMoviesByDate()
            }
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
            MovieEvents.HideMovieInfo -> {
                _state.update {it.copy(
                    isReadingInfo = false
                )}
            }
            is MovieEvents.ShowMovieInfo -> {
                _state.update { it.copy(
                    movieInfo = event.movie,
                    isReadingInfo = true
                )}
            }
            is MovieEvents.FavoriteMovie -> {
                viewModelScope.launch(Dispatchers.IO) {
                    dao.favoriteMovie(event.id)
                }
            }
        }
    }

    fun getAllGenres(){
        viewModelScope.launch{
            _genres.postValue(repository.getAllGenres())
        }
    }

    fun insertAllGenres(genres: List<GenreEntity>){
        viewModelScope.launch {
            repository.insertAllGenres(*genres.toTypedArray())
        }
    }
}

private operator fun <T> StateFlow<T>.get(id: Int): MovieEntity {
    return this[id]
}
