package com.example.movie.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.movie.database.model.GenreEntity

@Dao
interface GenreDao {
    @Upsert
    suspend fun insertGenre(genre: GenreEntity)

    @Delete
    suspend fun deleteGenre(genre: GenreEntity)

    @Query("SELECT * FROM GenreEntity WHERE name = :name")
    fun getGenreByName(name: String): GenreEntity

    @Query("SELECT * FROM GenreEntity WHERE id = :id")
    fun getGenreById(id: Int): GenreEntity

    @Query("SELECT * FROM GenreEntity")
    suspend fun getAllGenres(): List<GenreEntity>

    @Upsert
    suspend fun insertAllGenres(vararg genres: GenreEntity)
}