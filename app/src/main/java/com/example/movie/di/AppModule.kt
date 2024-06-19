package com.example.movie.di

import android.app.Application
import androidx.room.Room
import com.example.movie.database.MovieDatabase
import com.example.movie.database.dao.GenreDao
import com.example.movie.database.dao.MovieDao
import com.example.movie.database.dao.UserDao
import com.example.movie.viewmodel.repositories.GenreRepository
import com.example.movie.viewmodel.repositories.LoginRepository
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
    fun provideLoginRepository(dao: UserDao): LoginRepository{
        return LoginRepository(dao)
    }
    @Provides
    @Singleton
    fun provideGenreRepository(dao: GenreDao): GenreRepository{
        return GenreRepository(dao)
    }
    @Provides
    @Singleton
    fun provideMovieRepository(movieDao: MovieDao, userDao: UserDao): MovieRepository{
        return MovieRepository(movieDao, userDao)
    }
}