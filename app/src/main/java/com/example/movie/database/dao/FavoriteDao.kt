package com.example.movie.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.movie.database.model.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Upsert
    suspend fun favoriteMovie(favoriteMovie: FavoriteEntity)

    @Delete
    suspend fun removeMovie(favoriteMovie: FavoriteEntity)

    @Query("SELECT * FROM FavoriteEntity WHERE email = :email")
    fun getAllFavoritesFromUser(email: String): Flow<List<FavoriteEntity>>?
}