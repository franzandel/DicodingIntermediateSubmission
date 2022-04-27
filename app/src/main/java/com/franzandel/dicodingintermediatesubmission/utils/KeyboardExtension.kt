package com.franzandel.dicodingintermediatesubmission.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Created by Franz Andel <franz.andel@ovo.id>
 * on 27 April 2022.
 */

fun Activity.hideKeyboard() {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    // Find the currently focused view, so we can grab the correct window token from it.
    var view = currentFocus
    // If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}
