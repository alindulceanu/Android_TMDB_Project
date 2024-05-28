package com.example.movie.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movie.database.dao.GenreDao
import com.example.movie.database.model.GenreEntity


@Database(
    entities = [GenreEntity::class],
    version = 2
)
abstract class GenreDatabase: RoomDatabase() {
        abstract val dao: GenreDao
}