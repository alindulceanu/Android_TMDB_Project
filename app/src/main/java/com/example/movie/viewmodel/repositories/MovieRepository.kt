package com.example.movie.viewmodel.repositories

import androidx.lifecycle.SavedStateHandle
import com.example.movie.database.dao.MovieDao
import com.example.movie.database.dao.UserDao
import com.example.movie.database.model.MovieEntity
import com.example.movie.viewmodel.events.FilterType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieDao: MovieDao,
) {
    fun sortMovies(sortType: FilterType, favoriteIds: List<Int>? = null): Flow<List<MovieEntity>> {
        return when(sortType) {
            FilterType.POPULARITY -> movieDao.orderMoviesByPopularity()
            FilterType.FAVORITES -> {
                if (favoriteIds != null) {
                    movieDao.orderFavouriteMovies(favoriteIds)
                } else {
                    flowOf(emptyList())
                }
            }
            FilterType.RATING -> movieDao.orderMoviesByRating()
            FilterType.RELEASE_DATE -> movieDao.orderMoviesByDate()
        }
    }
//    suspend fun favoriteMovie(idDatabase: Int) {
//        movieDao.favoriteMovie(idDatabase)
//    }

    fun getMovieById(idDatabase: Int): Flow<MovieEntity?> {
        return movieDao.getMovieById(idDatabase)
    }

    suspend fun insertMovie(movie: MovieEntity) {
        movieDao.insertMovie(movie)
    }


}