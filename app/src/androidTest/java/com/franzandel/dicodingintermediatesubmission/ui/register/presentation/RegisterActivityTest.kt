package com.franzandel.dicodingintermediatesubmission.ui.register.presentation

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
import com.franzandel.dicodingintermediatesubmission.ui.register.RegisterActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Franz Andel
 * on 31 August 2022.
 */

@LargeTest
@HiltAndroidTest
class RegisterActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        ActivityScenario.launch(RegisterActivity::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun check_if_register_works_correctly() {
        mockWebServer.enqueueResponse("register_response.json")
        mockWebServer.enqueueResponse("login_response.json")

        onView(withId(R.id.layout_register)).check(matches(isDisplayed()))
        onView(withId(R.id.et_name)).perform(CustomEditText.setText("vcxz"))
        onView(withId(R.id.et_username)).perform(CustomEditText.setText("vcxz4321@gmail.com"))
        onView(withId(R.id.et_password)).perform(CustomEditText.setText("vcxzvcxz"))
        onView(withId(R.id.et_confirmation_password)).perform(CustomEditText.setText("vcxzvcxz"))
        onView(withId(R.id.btn_register)).perform(click())
        onView(withId(R.id.srl_home)).check(matches(isDisplayed()))
    }

    @Test
    fun check_if_register_got_email_validation() {
        mockWebServer.enqueueResponse("register_email_validation_response.json", responseCode = 400)

        onView(withId(R.id.layout_register)).check(matches(isDisplayed()))
        onView(withId(R.id.et_name)).perform(CustomEditText.setText("vcxz"))
        onView(withId(R.id.et_username)).perform(CustomEditText.setText("vcxz4321@gmail.com"))
        onView(withId(R.id.et_password)).perform(CustomEditText.setText("vcxzvcxz"))
        onView(withId(R.id.et_confirmation_password)).perform(CustomEditText.setText("vcxzvcxz"))
        onView(withId(R.id.btn_register)).perform(click())
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.register_already_exist)))
    }

    @Test
    fun check_if_register_got_general_error() {
        mockWebServer.enqueueResponse("register_general_error_response.json", responseCode = 400)

        onView(withId(R.id.layout_register)).check(matches(isDisplayed()))
        onView(withId(R.id.et_name)).perform(CustomEditText.setText("vcxz"))
        onView(withId(R.id.et_username)).perform(CustomEditText.setText("vcxz4321@gmail.com"))
        onView(withId(R.id.et_password)).perform(CustomEditText.setText("vcxzvcxz"))
        onView(withId(R.id.et_confirmation_password)).perform(CustomEditText.setText("vcxzvcxz"))
        onView(withId(R.id.btn_register)).perform(click())
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.register_failed)))
    }

    @Test
    fun check_if_register_got_exception_error() {
        mockWebServer.enqueueResponse("empty_response.json")

        onView(withId(R.id.layout_register)).check(matches(isDisplayed()))
        onView(withId(R.id.et_name)).perform(CustomEditText.setText("vcxz"))
        onView(withId(R.id.et_username)).perform(CustomEditText.setText("vcxz4321@gmail.com"))
        onView(withId(R.id.et_password)).perform(CustomEditText.setText("vcxzvcxz"))
        onView(withId(R.id.et_confirmation_password)).perform(CustomEditText.setText("vcxzvcxz"))
        onView(withId(R.id.btn_register)).perform(click())
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.system_error)))
    }

    @Test
    fun check_if_register_got_name_validation() {
        onView(withId(R.id.layout_register)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_register)).perform(click())
        onView(withId(R.id.et_name)).check(matches(CustomEditText.checkValidation(R.string.empty_name)))
    }

    @Test
    fun check_if_register_got_username_empty_validation() {
        onView(withId(R.id.layout_register)).check(matches(isDisplayed()))
        onView(withId(R.id.et_name)).perform(CustomEditText.setText("vcxz"))
        onView(withId(R.id.btn_register)).perform(click())
        onView(withId(R.id.et_username)).check(matches(CustomEditText.checkValidation(R.string.empty_username)))
    }

    @Test
    fun check_if_register_got_username_invalid_format_validation() {
        onView(withId(R.id.layout_register)).check(matches(isDisplayed()))
        onView(withId(R.id.et_name)).perform(CustomEditText.setText("vcxz"))
        onView(withId(R.id.et_username)).perform(CustomEditText.setText("asdf"))
        onView(withId(R.id.btn_register)).perform(click())
        onView(withId(R.id.et_username)).check(matches(CustomEditText.checkValidation(R.string.invalid_username)))
    }

    @Test
    fun check_if_register_got_password_validation() {
        onView(withId(R.id.layout_register)).check(matches(isDisplayed()))
        onView(withId(R.id.et_name)).perform(CustomEditText.setText("vcxz"))
        onView(withId(R.id.et_username)).perform(CustomEditText.setText("asdf@gmail.com"))
        onView(withId(R.id.btn_register)).perform(click())
        onView(withId(R.id.et_password)).check(matches(CustomEditText.checkValidation(R.string.invalid_password)))
    }

    @Test
    fun check_if_register_got_confirmation_password_empty_validation() {
        onView(withId(R.id.layout_register)).check(matches(isDisplayed()))
        onView(withId(R.id.et_name)).perform(CustomEditText.setText("vcxz"))
        onView(withId(R.id.et_username)).perform(CustomEditText.setText("asdf@gmail.com"))
        onView(withId(R.id.et_password)).perform(CustomEditText.setText("vcxzvcxz"))
        onView(withId(R.id.btn_register)).perform(click())
        onView(withId(R.id.et_confirmation_password)).check(matches(CustomEditText.checkValidation(R.string.empty_confirmation_password)))
    }

    @Test
    fun check_if_register_got_confirmation_password_not_match_validation() {
        onView(withId(R.id.layout_register)).check(matches(isDisplayed()))
        onView(withId(R.id.et_name)).perform(CustomEditText.setText("vcxz"))
        onView(withId(R.id.et_username)).perform(CustomEditText.setText("asdf@gmail.com"))
        onView(withId(R.id.et_password)).perform(CustomEditText.setText("vcxzvcxz"))
        onView(withId(R.id.et_confirmation_password)).perform(CustomEditText.setText("vcxz"))
        onView(withId(R.id.btn_register)).perform(click())
        onView(withId(R.id.et_confirmation_password)).check(matches(CustomEditText.checkValidation(R.string.invalid_confirmation_password)))
    }
}
