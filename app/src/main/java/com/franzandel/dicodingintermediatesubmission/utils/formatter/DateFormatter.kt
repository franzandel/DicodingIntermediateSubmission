package com.franzandel.dicodingintermediatesubmission.utils.formatter

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Created by Franz Andel
 * on 21 July 2022.
 */

object DateFormatter {

    fun formatLongDate(date: String): String {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
            sdf.parse(date)?.let {
                DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT).format(it)
            }.orEmpty()
        } catch (e: ParseException) {
            ""
        }
    }
}
