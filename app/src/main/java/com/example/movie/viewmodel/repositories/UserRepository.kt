package com.example.movie.viewmodel.repositories

import com.example.movie.database.dao.UserDao
import com.example.movie.database.model.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    suspend fun insertUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    fun getUserByUsername(username: String): Flow<UserEntity?> {
        return userDao.getUserByUsername(username)
    }

    fun getUserByEmail(email: String): Flow<UserEntity?> {
        return userDao.getUserByEmail(email)
    }

    fun getSavedUsers(): Flow<UserEntity?> {
        return userDao.getSavedUser()
    }

    suspend fun saveUser(username: String) {
        userDao.saveUser(username)
    }

    suspend fun disconnectUser(username: String) {
        userDao.disconnectUser(username)
    }

    suspend fun activateUser(username: String) {
        userDao.activateUser(username)
    }

    fun getActiveUser(): Flow<UserEntity>? {
        return userDao.getActiveUser()
    }
}