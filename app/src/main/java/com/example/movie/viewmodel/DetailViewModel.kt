package com.example.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie.data.remote.MovieService
import com.example.movie.database.model.GenreEntity
import com.example.movie.database.model.MovieEntity
import com.example.movie.viewmodel.repositories.GenreRepository
import com.example.movie.viewmodel.repositories.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val genreRepository: GenreRepository,
    private val movieRepository: MovieRepository
) : ViewModel() {
    init {
        val service: MovieService = MovieService.create()

        viewModelScope.launch {
            insertAllGenres(
                service.getGenres().genres
            )
        }
    }

    private val _genres = MutableLiveData<List<GenreEntity>>()
    val genres : LiveData<List<GenreEntity>> get() = _genres

    private val _movie = MutableLiveData<MovieEntity?>()
    val movie: LiveData<MovieEntity?> get() = _movie
    fun fetchItemById(idDatabase: Int) {
        viewModelScope.launch {
            movieRepository.getMovieById(idDatabase).collect{ fetchedMovie ->
                _movie.postValue(fetchedMovie)
            }
        }
    }
    fun getAllGenres(){
        viewModelScope.launch{
            _genres.postValue(genreRepository.getAllGenres())
        }
    }
    private suspend fun insertAllGenres(genres: List<GenreEntity>){
        genreRepository.insertAllGenres(*genres.toTypedArray())
    }

    fun favoriteMovie(){
        viewModelScope.launch {
            _movie.value?.let{currentMovie ->
                movieRepository.favoriteMovie(currentMovie.idDatabase)
            }
        }
    }
}
