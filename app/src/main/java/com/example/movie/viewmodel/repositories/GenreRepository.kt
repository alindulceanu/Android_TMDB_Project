package com.example.movie.viewmodel.repositories

import androidx.room.Query
import com.example.movie.database.dao.GenreDao
import com.example.movie.database.model.GenreEntity

class GenreRepository(private val genreDao: GenreDao) {

    suspend fun getGenreByName(name: String): GenreEntity {
        return genreDao.getGenreByName(name)
    }


    suspend fun getGenreById(id: Int): GenreEntity {
        return genreDao.getGenreById(id)
    }

    suspend fun getAllGenres(): List<GenreEntity>{
        return genreDao.getAllGenres()
    }
    suspend fun insertAllGenres(vararg genres: GenreEntity){
        genreDao.insertAllGenres(*genres)
    }
}