package com.franzandel.dicodingintermediatesubmission.test

import androidx.test.espresso.idling.CountingIdlingResource

/**
 * Created by Franz Andel
 * on 30 August 2022.
 */

object EspressoIdlingResource {

    private const val STORIES = "stories"
    val countingIdlingResource = CountingIdlingResource(STORIES)

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
}

inline fun <T> wrapEspressoIdlingResource(function: () -> T): T {
    EspressoIdlingResource.increment() // Set app as busy.
    return try {
        function()
    } finally {
        EspressoIdlingResource.decrement() // Set app as idle.
    }
}
