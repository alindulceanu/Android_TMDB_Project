package com.example.movie.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity (
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["email"],
        childColumns = ["email"],
        onDelete = ForeignKey.CASCADE
    ),ForeignKey(
        entity = MovieEntity::class,
        parentColumns = ["idDatabase"],
        childColumns = ["id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["idFavorite"], unique = true)]
)
data class FavoriteEntity (
    @PrimaryKey(autoGenerate = true)
    val idFavorite: Int = 0,

    val id: Int,
    val email: String,
)