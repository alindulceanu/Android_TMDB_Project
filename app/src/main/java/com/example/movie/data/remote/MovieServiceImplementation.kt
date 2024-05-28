package com.example.movie.data.remote

import com.example.movie.data.remote.dto.MovieResponse
import com.example.movie.data.remote.dto.MovieResponseGenres
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class MovieServiceImplementation(
    private val client : HttpClient
) : MovieService {

    override suspend fun getMovies(): MovieResponse {
        val response: MovieResponse =
            client.get(HttpRoutes.POPULAR_URL) {
                headers {
                    append("accept", "application/json")
                    append(
                        "Authorization",
                        "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIzOGFkOTIwYWNkYjkyZGJlOTMzZjE5ZWVkMTU3MjZkMiIsInN1YiI6IjY2MzBiMWZiOTVjMGFmMDEyOWRhYzcyMSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.th1jrb984uhztON6hQdWW0QLLKcfPuqo-wvq5tJo8SY"
                    )
                }
            }.body<MovieResponse>()

//        val responseTxt = response.bodyAsText()
//        println("JSON received: $response")
        return response
//        return Json { ignoreUnknownKeys = true }.decodeFromString(
//            MovieResponse.serializer(),
//            responseTxt
//        )
    }

    override suspend fun getGenres(): MovieResponseGenres {
        val response: MovieResponseGenres =
            client.get(HttpRoutes.GENRE_LIST) {
                headers {
                    append("accept", "application/json")
                    append(
                        "Authorization",
                        "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIzOGFkOTIwYWNkYjkyZGJlOTMzZjE5ZWVkMTU3MjZkMiIsInN1YiI6IjY2MzBiMWZiOTVjMGFmMDEyOWRhYzcyMSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.th1jrb984uhztON6hQdWW0QLLKcfPuqo-wvq5tJo8SY"
                    )
                }
            }.body<MovieResponseGenres>()
        return response
    }
}
