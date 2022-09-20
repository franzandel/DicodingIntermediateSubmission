package com.franzandel.dicodingintermediatesubmission.ui.login

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.custom.CustomEditText
import com.franzandel.dicodingintermediatesubmission.test.EspressoIdlingResource
import com.franzandel.dicodingintermediatesubmission.test.enqueueResponse
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Franz Andel
 * on 30 August 2022.
 */

@LargeTest
@HiltAndroidTest
class LoginActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        ActivityScenario.launch(LoginActivity::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun check_if_login_works_correctly() {
        mockWebServer.enqueueResponse("login_response.json")

        onView(withId(R.id.layout_login)).check(matches(isDisplayed()))
        onView(withId(R.id.et_username)).perform(CustomEditText.setText("asdf@gmail.com"))
        onView(withId(R.id.et_password)).perform(CustomEditText.setText("asdfasdf"))
        onView(withId(R.id.btn_login)).perform(click())
        onView(withId(R.id.srl_home)).check(matches(isDisplayed()))
    }

    @Test
    fun check_if_login_got_error() {
        mockWebServer.enqueueResponse("login_error_response.json", responseCode = 400)

        onView(withId(R.id.layout_login)).check(matches(isDisplayed()))
        onView(withId(R.id.et_username)).perform(CustomEditText.setText("asdf@gmail.com"))
        onView(withId(R.id.et_password)).perform(CustomEditText.setText("asdfasdf"))
        onView(withId(R.id.btn_login)).perform(click())
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.login_failed)))
    }

    @Test
    fun check_if_login_got_exception_error() {
        mockWebServer.enqueueResponse("empty_response.json")

        onView(withId(R.id.layout_login)).check(matches(isDisplayed()))
        onView(withId(R.id.et_username)).perform(CustomEditText.setText("asdf@gmail.com"))
        onView(withId(R.id.et_password)).perform(CustomEditText.setText("asdfasdf"))
        onView(withId(R.id.btn_login)).perform(click())
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.system_error)))
    }

    @Test
    fun check_if_login_got_username_empty_validation() {
        onView(withId(R.id.layout_login)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_login)).perform(click())
        onView(withId(R.id.et_username)).check(matches(CustomEditText.checkValidation(R.string.empty_username)))
    }

    @Test
    fun check_if_login_got_username_invalid_format_validation() {
        onView(withId(R.id.layout_login)).check(matches(isDisplayed()))
        onView(withId(R.id.et_username)).perform(CustomEditText.setText("asdf"))
        onView(withId(R.id.btn_login)).perform(click())
        onView(withId(R.id.et_username)).check(matches(CustomEditText.checkValidation(R.string.invalid_username)))
    }

    @Test
    fun check_if_login_got_password_validation() {
        mockWebServer.enqueueResponse("login_error_response.json", responseCode = 400)

        onView(withId(R.id.layout_login)).check(matches(isDisplayed()))
        onView(withId(R.id.et_username)).perform(CustomEditText.setText("asdf@gmail.com"))
        onView(withId(R.id.btn_login)).perform(click())
        onView(withId(R.id.et_password)).check(matches(CustomEditText.checkValidation(R.string.invalid_password)))
    }

    @Test
    fun check_if_login_navigate_to_register_successfully() {
        onView(withId(R.id.layout_login)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_no_account_yet)).perform(click())
        onView(withId(R.id.layout_register)).check(matches(isDisplayed()))
    }
}
