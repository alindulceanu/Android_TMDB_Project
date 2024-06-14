package com.example.movie.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.movie.database.GenreDatabase
import com.example.movie.database.MovieDatabase
import com.example.movie.database.dao.GenreDao
import com.example.movie.database.dao.MovieDao
import com.example.movie.viewmodel.repositories.GenreRepository
import com.example.movie.viewmodel.repositories.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideMovieDatabase(app: Application): MovieDatabase{
        return Room
            .databaseBuilder(
                app,
                MovieDatabase::class.java,
                "movie_db")
            .fallbackToDestructiveMigration()
            .build()
    }
    @Provides
    @Singleton
    fun provideGenreDatabase(app: Application): GenreDatabase {
        return Room
            .databaseBuilder(
                app,
                GenreDatabase::class.java,
                "genre_db")
            .build()
    }
    @Provides
    @Singleton
    fun provideMovieDao(db: MovieDatabase): MovieDao {
        return db.dao
    }
    @Provides
    @Singleton
    fun provideGenreDao(db: GenreDatabase): GenreDao {
        return db.dao
    }
    @Provides
    @Singleton
    fun provideGenreRepository(dao: GenreDao): GenreRepository{
        return GenreRepository(dao)
    }
    @Provides
    @Singleton
    fun provideMovieRepository(dao: MovieDao): MovieRepository{
        return MovieRepository(dao)
    }
}