package com.example.movie.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.movie.database.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Upsert
    suspend fun insertUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query("SELECT * FROM UserEntity WHERE userName = :userName")
    fun getUserByUsername(userName: String): Flow<UserEntity?>

    @Query("SELECT * FROM UserEntity WHERE email = :email")
    fun getUserByEmail(email: String): Flow<UserEntity?>

    @Query("SELECT * FROM UserEntity WHERE isSaved = 1 LIMIT 1")
    fun getSavedUser(): Flow<UserEntity?>

    @Query("UPDATE UserEntity SET isSaved = 1 WHERE userName = :username")
    suspend fun saveUser(username: String)

    @Query("UPDATE UserEntity SET isSaved = 0 WHERE userName = :username")
    suspend fun disconnectUser(username: String)
}