package com.example.movie.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.movie.database.dao.FavoriteDao
import com.example.movie.database.dao.GenreDao
import com.example.movie.database.dao.MovieDao
import com.example.movie.database.dao.UserDao
import com.example.movie.database.model.FavoriteEntity
import com.example.movie.database.model.GenreEntity
import com.example.movie.database.model.MovieEntity
import com.example.movie.database.model.UserEntity

@Database(
    entities = [MovieEntity::class, GenreEntity::class, UserEntity::class, FavoriteEntity::class],
    version = 11
)
abstract class MovieDatabase: RoomDatabase() {
    abstract val movieDao: MovieDao
    abstract val genreDao: GenreDao
    abstract val userDao: UserDao
    abstract val favoriteDao: FavoriteDao
}

//val MIGRATION_1_2 = object : Migration(1, 2) {
//    override fun migrate(database: SupportSQLiteDatabase) {
//        // Since we are not modifying any existing tables, we just add a new table
//        database.execSQL("CREATE TABLE IF NOT EXISTS `new_table` (`id` INTEGER, `name` TEXT, PRIMARY KEY(`id`))")
//        // If adding a new column to an existing table, you would use:
//        // database.execSQL("ALTER TABLE MovieEntity ADD COLUMN new_column_name TEXT NOT NULL DEFAULT ''")
//    }
//}