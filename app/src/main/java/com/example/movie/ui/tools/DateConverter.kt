package com.example.movie.ui.tools

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    private val dbFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val displayFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)

    fun fromDbDateString(dateString: String?): Date? {
        return dateString?.let {
            dbFormat.parse(it)
        }
    }

    fun toDbDateString(date: Date?): String? {
        return date?.let {
            dbFormat.format(it)
        }
    }

    fun toDisplayDateString(dateString: String?): String? {
        val date = fromDbDateString(dateString)
        return date?.let {
            displayFormat.format(it)
        }
    }
}

