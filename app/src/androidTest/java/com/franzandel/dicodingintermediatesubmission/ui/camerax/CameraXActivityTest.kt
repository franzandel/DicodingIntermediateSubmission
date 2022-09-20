package com.franzandel.dicodingintermediatesubmission.ui.camerax

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
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
class CameraXActivityTest {

    @Before
    fun setUp() {
        ActivityScenario.launch(CameraXActivity::class.java)
    }

    @Test
    fun check_if_back_camera_works_correctly() {
        onView(withId(R.id.layout_camera_x)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_capture_image)).perform(click())
        onView(withId(R.id.layout_loading)).check(matches(isDisplayed()))
    }

    @Test
    fun check_if_front_camera_works_correctly() {
        onView(withId(R.id.layout_camera_x)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_switch_camera)).perform(click())
        onView(withId(R.id.iv_capture_image)).perform(click())
        onView(withId(R.id.layout_loading)).check(matches(isDisplayed()))
    }
}
