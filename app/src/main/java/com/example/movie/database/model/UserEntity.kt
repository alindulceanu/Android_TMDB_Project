package com.example.movie.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity (
    @PrimaryKey
    val email: String,
    val userName: String,
    val password: String,
    var isSaved: Boolean = false,
    var isActive: Boolean = false,
)