package com.example.movie.viewmodel.repositories

import com.example.movie.database.dao.MovieDao
import com.example.movie.database.model.MovieEntity
import com.example.movie.viewmodel.events.FilterType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieDao: MovieDao
) {
    suspend fun sortMovies(sortType: FilterType): Flow<List<MovieEntity>> {
        return when(sortType) {
            FilterType.POPULARITY -> movieDao.orderMoviesByPopularity()
            FilterType.FAVORITES -> movieDao.orderFavouriteMovies()
            FilterType.RATING -> movieDao.orderMoviesByRating()
            FilterType.RELEASE_DATE -> movieDao.orderMoviesByDate()
        }
    }
    suspend fun favoriteMovie(id: Int) {
        movieDao.favoriteMovie(id)
    }

    fun getMovieById(id: Int): Flow<MovieEntity?> {
        return movieDao.getMovieByID(id)
    }

    suspend fun insertMovie(movie: MovieEntity) {
        movieDao.insertMovie(movie)
    }
}