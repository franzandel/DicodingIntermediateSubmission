package com.franzandel.dicodingintermediatesubmission.ui.maps

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.test.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created by Franz Andel
 * on 16 September 2022.
 */

@LargeTest
class MapsActivityTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        ActivityScenario.launch(MapsActivity::class.java)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun check_if_normal_map_shows_correctly() {
        onView(withId(R.id.map)).check(matches(isDisplayed()))
    }

    @Test
    fun check_if_satellite_map_shows_correctly() {
        openActionBarOverflowOrOptionsMenu(context)
        onView(
            withText(context.getString(R.string.satellite_type))
        ).perform(click())
        onView(withId(R.id.map)).check(matches(isDisplayed()))
    }

    @Test
    fun check_if_terrain_map_shows_correctly() {
        openActionBarOverflowOrOptionsMenu(context)
        onView(
            withText(context.getString(R.string.terrain_type))
        ).perform(click())
        onView(withId(R.id.map)).check(matches(isDisplayed()))
    }

    @Test
    fun check_if_hybrid_map_shows_correctly() {
        openActionBarOverflowOrOptionsMenu(context)
        onView(
            withText(context.getString(R.string.hybrid_type))
        ).perform(click())
        onView(withId(R.id.map)).check(matches(isDisplayed()))
    }
}
