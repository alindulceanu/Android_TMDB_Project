package com.example.movie.viewmodel.repositories

import com.example.movie.database.dao.GenreDao
import com.example.movie.database.model.GenreEntity
import javax.inject.Inject

class GenreRepository @Inject constructor(
    private val genreDao: GenreDao
) {

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