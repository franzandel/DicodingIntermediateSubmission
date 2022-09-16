package com.franzandel.dicodingintermediatesubmission.ui.addstory

import android.Manifest
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.rule.GrantPermissionRule
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.custom.CustomEditText
import com.franzandel.dicodingintermediatesubmission.custom.ImageViewMatcher
import com.franzandel.dicodingintermediatesubmission.custom.lazyActivityScenarioRule
import com.franzandel.dicodingintermediatesubmission.test.EspressoIdlingResource
import com.franzandel.dicodingintermediatesubmission.test.enqueueResponse
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Franz Andel
 * on 09 September 2022.
 */

@LargeTest
@HiltAndroidTest
class AddStoryActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val rule = lazyActivityScenarioRule<AddStoryActivity>()

    @get:Rule(order = 2)
    val cameraPermission: GrantPermissionRule =
        GrantPermissionRule.grant(Manifest.permission.CAMERA)

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun check_if_choose_image_from_camera_success() {
        mockWebServer.enqueueResponse("upload_image_response.json")
        onView(withId(R.id.btn_from_camera)).perform(click())
        onView(withId(R.id.iv_add_story)).check(matches(ImageViewMatcher.hasDrawable()))
        onView(withId(R.id.et_description)).perform(CustomEditText.setText("asdf"))
        onView(withId(R.id.btn_upload)).perform(scrollTo(), click())
        assertTrue(rule.getScenario().state == Lifecycle.State.DESTROYED)
    }

    @Test
    fun check_if_choose_image_from_camera_with_location_success() {
        mockWebServer.enqueueResponse("upload_image_response.json")
        onView(withId(R.id.btn_from_camera)).perform(click())
        onView(withId(R.id.iv_add_story)).check(matches(ImageViewMatcher.hasDrawable()))
        onView(withId(R.id.cb_current_location)).perform(click())
        onView(withId(R.id.tv_current_location)).check(matches(isDisplayed()))
        onView(withId(R.id.et_description)).perform(CustomEditText.setText("asdf"))
        onView(withId(R.id.btn_upload)).perform(scrollTo(), click())
        assertTrue(rule.getScenario().state == Lifecycle.State.DESTROYED)
    }

    @Test
    fun check_if_choose_image_from_gallery_success() {
        mockWebServer.enqueueResponse("upload_image_response.json")
        onView(withId(R.id.btn_from_gallery)).perform(click())
        onView(withId(R.id.iv_add_story)).check(matches(ImageViewMatcher.hasDrawable()))
        onView(withId(R.id.et_description)).perform(CustomEditText.setText("asdf"))
        onView(withId(R.id.btn_upload)).perform(scrollTo(), click())
        assertTrue(rule.getScenario().state == Lifecycle.State.DESTROYED)
    }

    @Test
    fun check_if_choose_image_from_gallery_with_location_success() {
        mockWebServer.enqueueResponse("upload_image_response.json")
        onView(withId(R.id.btn_from_gallery)).perform(click())
        onView(withId(R.id.iv_add_story)).check(matches(ImageViewMatcher.hasDrawable()))
        onView(withId(R.id.cb_current_location)).perform(click())
        onView(withId(R.id.tv_current_location)).check(matches(isDisplayed()))
        onView(withId(R.id.et_description)).perform(CustomEditText.setText("asdf"))
        onView(withId(R.id.btn_upload)).perform(scrollTo(), click())
        assertTrue(rule.getScenario().state == Lifecycle.State.DESTROYED)
    }

    @Test
    fun check_if_get_choose_image_validation() {
        onView(withId(R.id.et_description)).perform(CustomEditText.setText("asdf"))
        onView(withId(R.id.btn_upload)).perform(scrollTo(), click())
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.add_story_select_image)))
    }

    @Test
    fun check_if_get_description_empty_validation() {
        onView(withId(R.id.btn_from_gallery)).perform(click())
        onView(withId(R.id.iv_add_story)).check(matches(ImageViewMatcher.hasDrawable()))
        onView(withId(R.id.btn_upload)).perform(scrollTo(), click())
        onView(withId(R.id.et_description)).check(matches(CustomEditText.checkValidation(R.string.add_story_empty_description)))
    }
}
