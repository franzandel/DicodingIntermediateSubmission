package com.franzandel.dicodingintermediatesubmission.ui.splashscreen

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import com.franzandel.dicodingintermediatesubmission.R
import org.junit.Before
import org.junit.Test

/**
 * Created by Franz Andel
 * on 30 August 2022.
 */

@LargeTest
class SplashScreenActivityTest {

    @Before
    fun setup() {
        ActivityScenario.launch(SplashScreenActivity::class.java)
    }

    @Test
    fun checkIfNavigatedCorrectly() {
        onView(withId(R.id.layout_splash_screen)).check(matches(isDisplayed()))
    }
}
