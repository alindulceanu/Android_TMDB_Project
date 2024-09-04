package com.example.movie.database.model
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient


@Entity(
    indices = [Index(value = ["id"], unique = true)]
)
@Serializable
@TypeConverters(IntListConverter::class)
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    @Transient
    val idDatabase : Int = 0,

    val adult : Boolean,
    @SerialName("backdrop_path") val backdropPath : String?,

    @SerialName("genre_ids")
    val genreIds : List<Int>,

    val id : Int,
    @SerialName("original_language")val originalLanguage : String,
    @SerialName("original_title")val originalTitle : String,
    val overview : String,
    val popularity : Double,
    @SerialName("poster_path")val posterPath : String?,
    @SerialName("release_date")@Contextual val releaseDate : String,
    val title : String,
    val video : Boolean,
    @SerialName("vote_average")val voteAverage : Double,
    @SerialName("vote_count")val voteCount : Int
) : java.io.Serializable