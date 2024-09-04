package com.example.movie.viewmodel.repositories

import com.example.movie.database.dao.FavoriteDao
import com.example.movie.database.model.FavoriteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteRepository @Inject constructor(
    private val favoriteDao: FavoriteDao
) {

    suspend fun favoriteMovie(email: String, id: Int){
        favoriteDao.favoriteMovie(FavoriteEntity(id = id, email = email))
    }

    suspend fun removeMovie(email: String, id: Int){
        favoriteDao.removeMovie(FavoriteEntity(id = id, email = email))
    }

    fun getAllFavoritesFromUser(email: String): Flow<List<FavoriteEntity>>? {
        return favoriteDao.getAllFavoritesFromUser(email)
    }
}