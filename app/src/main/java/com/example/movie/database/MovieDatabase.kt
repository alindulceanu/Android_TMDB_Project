package com.example.movie.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.movie.database.dao.MovieDao
import com.example.movie.database.model.MovieEntity

@Database(
    entities = [MovieEntity::class],
    version = 5
)
abstract class MovieDatabase: RoomDatabase() {
    abstract val dao: MovieDao
}

//val MIGRATION_1_2 = object : Migration(1, 2) {
//    override fun migrate(database: SupportSQLiteDatabase) {
//        // Since we are not modifying any existing tables, we just add a new table
//        database.execSQL("CREATE TABLE IF NOT EXISTS `new_table` (`id` INTEGER, `name` TEXT, PRIMARY KEY(`id`))")
//        // If adding a new column to an existing table, you would use:
//        // database.execSQL("ALTER TABLE MovieEntity ADD COLUMN new_column_name TEXT NOT NULL DEFAULT ''")
//    }
//}