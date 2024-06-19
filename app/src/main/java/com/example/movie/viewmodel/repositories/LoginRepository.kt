package com.example.movie.viewmodel.repositories

import com.example.movie.database.dao.UserDao
import com.example.movie.database.model.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val userDao: UserDao
) {
    suspend fun insertUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    fun getUserByUsername(username: String): Flow<UserEntity?> {
        return userDao.getUserByUsername(username)
            .map { it ?: null }
    }

    fun getUserByEmail(email: String): Flow<UserEntity?> {
        return userDao.getUserByEmail(email)
            .map { it ?: null }
    }

    fun getSavedUsers(): Flow<UserEntity?> {
        return userDao.getSavedUser()
            .map { it ?: null}
    }

    suspend fun saveUser(username: String) {
        userDao.saveUser(username)
    }
}