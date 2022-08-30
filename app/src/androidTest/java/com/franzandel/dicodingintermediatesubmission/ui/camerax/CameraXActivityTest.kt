package com.franzandel.dicodingintermediatesubmission.ui.camerax

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.LargeTest
import com.franzandel.dicodingintermediatesubmission.R
import org.junit.Before
import org.junit.Test

/**
 * Created by Franz Andel
 * on 30 August 2022.
 */

@LargeTest
class CameraXActivityTest {

    @Before
    fun setUp() {
        ActivityScenario.launch(CameraXActivity::class.java)
    }

    @Test
    fun checkIfCameraXDisplayedCorrectly() {
        Espresso.onView(ViewMatchers.withId(R.id.layout_camera_x))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.iv_capture_image)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.layout_loading))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun checkIfSwitchCameraWorksCorrectly() {
        Espresso.onView(ViewMatchers.withId(R.id.layout_camera_x))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.iv_switch_camera)).perform(ViewActions.click())
    }
}
