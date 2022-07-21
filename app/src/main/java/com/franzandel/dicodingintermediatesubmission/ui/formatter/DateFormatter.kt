package com.franzandel.dicodingintermediatesubmission.ui.formatter

import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import java.util.Locale

/**
 * Created by Franz Andel <franz.andel@ovo.id>
 * on 21 July 2022.
 */

object DateFormatter {

    fun formatLongDate(date: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SZ", Locale.getDefault())
        return DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT)
            .format(sdf.parse(date))
    }
}
