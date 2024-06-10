package com.example.movie.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movie.database.dao.MovieDao
import com.example.movie.viewmodel.repositories.GenreRepository


class MovieViewModelFactory(
    private val dao: MovieDao,
    private val repository: GenreRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            return MovieViewModel(dao, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}