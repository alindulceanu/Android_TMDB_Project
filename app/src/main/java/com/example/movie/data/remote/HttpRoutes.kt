package com.example.movie.data.remote

object HttpRoutes {
    private const val BASE_URL = "https://api.themoviedb.org/3"
    const val BASE_URL_IMAGE = "https://image.tmdb.org/t/p/"
    const val ORIGINAL_SIZE = "original"
    const val W500_SIZE = "w500"

    const val POPULAR_URL = "${BASE_URL}/movie/popular?language=en-US&page=1"
    const val GENRE_LIST = "${BASE_URL}/genre/movie/list?language=en'"

}