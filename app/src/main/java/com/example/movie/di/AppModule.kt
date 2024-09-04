package com.example.movie.di

import android.app.Application
import androidx.room.Room
import com.example.movie.database.MovieDatabase
import com.example.movie.database.dao.FavoriteDao
import com.example.movie.database.dao.GenreDao
import com.example.movie.database.dao.MovieDao
import com.example.movie.database.dao.UserDao
import com.example.movie.viewmodel.repositories.FavoriteRepository
import com.example.movie.viewmodel.repositories.GenreRepository
import com.example.movie.viewmodel.repositories.MovieRepository
import com.example.movie.viewmodel.repositories.UserRepository
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
    fun provideMovieDao(db: MovieDatabase): MovieDao {
        return db.movieDao
    }
    @Provides
    @Singleton
    fun provideGenreDao(db: MovieDatabase): GenreDao {
        return db.genreDao
    }
    @Provides
    @Singleton
    fun provideUserDao(db: MovieDatabase): UserDao {
        return db.userDao
    }

    @Provides
    @Singleton
    fun provideFavoriteDao(db: MovieDatabase): FavoriteDao {
        return db.favoriteDao
    }
    @Provides
    @Singleton
    fun provideUserRepository(dao: UserDao): UserRepository {
        return UserRepository(dao)
    }
    @Provides
    @Singleton
    fun provideGenreRepository(dao: GenreDao): GenreRepository{
        return GenreRepository(dao)
    }
    @Provides
    @Singleton
    fun provideMovieRepository(movieDao: MovieDao): MovieRepository{
        return MovieRepository(movieDao)
    }

    @Provides
    @Singleton
    fun provideFavoriteRepository(favoriteDao: FavoriteDao): FavoriteRepository {
        return FavoriteRepository(favoriteDao)
    }
}