package com.example.movie.ui

import android.app.Application
import android.content.Context
import android.graphics.Color
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.example.movie.R
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

class GenreColors @Inject constructor( context: Application){
    private val genreColorMap = mutableMapOf<String, Int>()

    init {
        initialize(context)
    }
    private fun initialize(context: Application) {
        genreColorMap["Adventure"] = ContextCompat.getColor(context, R.color.Adventure)
        genreColorMap["Fantasy"] = ContextCompat.getColor(context, R.color.Fantasy)
        genreColorMap["Animation"] = ContextCompat.getColor(context, R.color.Animation)
        genreColorMap["Drama"] = ContextCompat.getColor(context, R.color.Drama)
        genreColorMap["Horror"] = ContextCompat.getColor(context, R.color.Horror)
        genreColorMap["Action"] = ContextCompat.getColor(context, R.color.Action)
        genreColorMap["Comedy"] = ContextCompat.getColor(context, R.color.Comedy)
        genreColorMap["History"] = ContextCompat.getColor(context, R.color.History)
        genreColorMap["Western"] = ContextCompat.getColor(context, R.color.Western)
        genreColorMap["Thriller"] = ContextCompat.getColor(context, R.color.Thriller)
        genreColorMap["Crime"] = ContextCompat.getColor(context, R.color.Crime)
        genreColorMap["Documentary"] = ContextCompat.getColor(context, R.color.Documentary)
        genreColorMap["Science Fiction"] = ContextCompat.getColor(context, R.color.Science_Fiction)
        genreColorMap["Mystery"] = ContextCompat.getColor(context, R.color.Mystery)
        genreColorMap["Music"] = ContextCompat.getColor(context, R.color.Music)
        genreColorMap["Romance"] = ContextCompat.getColor(context, R.color.Romance)
        genreColorMap["Family"] = ContextCompat.getColor(context, R.color.Family)
        genreColorMap["War"] = ContextCompat.getColor(context, R.color.War)
        genreColorMap["TV Movie"] = ContextCompat.getColor(context, R.color.TV_Movie)
        genreColorMap["Unknown"] = ContextCompat.getColor(context, R.color.teal_200)
    }

    fun getColorForGenre(genreName: String): Int? {
        return genreColorMap[genreName] ?: genreColorMap["Unknown"]
    }

//    companion object {
//        fun create(context: Application): GenreColors {
//            return GenreColors(context)
//        }
//    }
}

