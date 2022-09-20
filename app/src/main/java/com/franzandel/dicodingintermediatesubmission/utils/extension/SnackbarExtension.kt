package com.franzandel.dicodingintermediatesubmission.utils.extension

//noinspection SuspiciousImport
import android.R
import android.app.Activity
import android.view.View
import com.google.android.material.snackbar.Snackbar

/**
 * Created by Franz Andel
 * on 29 May 2022.
 */

fun Activity.showDefaultSnackbar(text: String, duration: Int) {
    val rootView: View? = findViewById(R.id.content)
    if (rootView != null) {
        Snackbar.make(rootView, text, duration).show()
    }
}
