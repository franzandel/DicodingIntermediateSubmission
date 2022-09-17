package com.franzandel.dicodingintermediatesubmission.ui.home

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.custom.RecyclerViewMatcher
import com.franzandel.dicodingintermediatesubmission.data.database.StoriesDatabase
import com.franzandel.dicodingintermediatesubmission.data.local.serializer.homeDataStore
import com.franzandel.dicodingintermediatesubmission.test.EspressoIdlingResource
import com.franzandel.dicodingintermediatesubmission.test.RoomUtils
import com.franzandel.dicodingintermediatesubmission.test.enqueueResponse
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

/**
 * Created by Franz Andel
 * on 07 September 2022.
 */

@LargeTest
@HiltAndroidTest
class HomeActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var database: StoriesDatabase

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        hiltRule.inject()
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        ActivityScenario.launch(HomeActivity::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        database.close()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun check_if_stories_shown_successfully() {
        val storyEntities = RoomUtils.getStoryEntities()
        database.homeDao().insertAll(storyEntities)
        onView(withId(R.id.rv_home)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_home)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                storyEntities.size - 1
            )
        )
    }

    @Test
    fun check_if_empty_stories_shown_successfully() {
        mockWebServer.enqueueResponse("stories_empty_response.json")
        database.homeDao().insertAll(listOf())
        onView(withId(R.id.tv_empty_message)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_empty_message)).check(matches(isDisplayed()))
    }

    @Test
    fun check_if_navigate_to_detail_successfully() {
        val storyEntities = RoomUtils.getStoryEntities()
        database.homeDao().insertAll(storyEntities)
        onView(withId(R.id.rv_home)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.layout_detail)).check(matches(isDisplayed()))
    }

    @Test
    fun check_if_navigate_to_settings_language_successfully() {
        EspressoIdlingResource.decrement()
        onView(withId(R.id.menu_settings)).perform(click())
    }

    @Test
    fun check_if_logout_success() {
        EspressoIdlingResource.decrement()
        onView(withId(R.id.menu_logout)).perform(click())
        onView(withId(android.R.id.button1)).perform(click())
        onView(withId(R.id.layout_login)).check(matches(isDisplayed()))
    }

    @Test
    fun check_if_show_location_stories_only_overflow_success() {
        runBlocking {
            val storyEntitiesWithLocation = RoomUtils.getStoryEntitiesWithLocation()
            database.homeDao().insertAll(storyEntitiesWithLocation)

            val context: Context = ApplicationProvider.getApplicationContext()
            val locationPreference = context.homeDataStore.data
                .map { settings ->
                    settings.locationPreference
                }.first()

            if (locationPreference == 0) {
                openActionBarOverflowOrOptionsMenu(context)
                onView(
                    withText(
                        getInstrumentation().targetContext.getString(R.string.menu_location_preference)
                    )
                ).perform(click())
            }

            onView(
                RecyclerViewMatcher(R.id.rv_home).atPositionOnView(0, R.id.tv_location)
            ).check(matches(isDisplayed()))
        }
    }

    @Test
    fun check_if_show_all_stories_overflow_success() {
        EspressoIdlingResource.decrement()
        runBlocking {
            val storyEntities = RoomUtils.getStoryEntities()
            database.homeDao().insertAll(storyEntities)

            val context: Context = ApplicationProvider.getApplicationContext()
            val locationPreference = context.homeDataStore.data
                .map { settings ->
                    settings.locationPreference
                }.first()

            if (locationPreference == 1) {
                openActionBarOverflowOrOptionsMenu(context)
                onView(
                    withText(
                        getInstrumentation().targetContext.getString(R.string.menu_location_preference)
                    )
                ).perform(click())
            }

            onView(
                RecyclerViewMatcher(R.id.rv_home)
                    .atPositionOnView(0, R.id.tv_location)
            ).check(matches(not(isDisplayed())))
        }
    }

    @Test
    fun check_if_navigate_to_add_story_successfully() {
        EspressoIdlingResource.decrement()
        onView(withId(R.id.fab_add_story)).perform(click())
        onView(withId(R.id.layout_add_story)).check(matches(isDisplayed()))
    }
}
