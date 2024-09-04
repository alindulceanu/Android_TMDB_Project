package com.example.movie.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.movie.database.model.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Upsert
    suspend fun insertMovie(movie: MovieEntity)

    @Upsert
    suspend fun insertAll(movies: List<MovieEntity>)  // To insert a list of movies

    @Delete
    suspend fun deleteMovie(movie: MovieEntity)

//    @Query("UPDATE MovieEntity SET favourite = NOT favourite WHERE idDatabase = :idDatabase")
//    suspend fun favoriteMovie(idDatabase: Int)

    @Query("SELECT * FROM MovieEntity")
    fun getAllMovies(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM MovieEntity WHERE idDatabase = :idDatabase")
    fun getMovieById(idDatabase: Int): Flow<MovieEntity?>

//    @Query("SELECT * FROM MovieEntity WHERE genreIds = :genre")
//    suspend fun orderMoviesByGenre(genre: Int): Flow<List<MovieEntity>>

    @Query("SELECT * FROM MovieEntity ORDER BY popularity DESC")
    fun orderMoviesByPopularity(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM MovieEntity WHERE idDatabase IN (:favoriteIds) ORDER BY title")
    fun orderFavouriteMovies(favoriteIds: List<Int>): Flow<List<MovieEntity>>

    @Query("SELECT * FROM MovieEntity ORDER BY releaseDate DESC")
    fun orderMoviesByDate(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM MovieEntity ORDER BY voteAverage DESC")
    fun orderMoviesByRating(): Flow<List<MovieEntity>>
}