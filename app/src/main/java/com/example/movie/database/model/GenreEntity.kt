package com.example.movie.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class GenreEntity(
    @PrimaryKey
    val id: Int,
    val name: String
)
