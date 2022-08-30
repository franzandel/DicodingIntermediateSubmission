package com.franzandel.dicodingintermediatesubmission.ui.login

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.custom.CustomEditText
import com.franzandel.dicodingintermediatesubmission.test.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created by Franz Andel
 * on 30 August 2022.
 */

@LargeTest
class LoginActivityTest {

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        ActivityScenario.launch(LoginActivity::class.java)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun check_if_login_works_correctly() {
        onView(withId(R.id.layout_login)).check(matches(isDisplayed()))
        onView(withId(R.id.et_username)).perform(CustomEditText.setText("asdf@gmail.com"))
        onView(withId(R.id.et_password)).perform(CustomEditText.setText("asdfasdf"))
        onView(withId(R.id.btn_login)).perform(ViewActions.click())
        onView(withId(R.id.srl_home)).check(matches(isDisplayed()))
    }

    @Test
    fun check_if_login_got_username_validation() {
        onView(withId(R.id.layout_login)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_login)).perform(ViewActions.click())
        onView(withId(R.id.et_username)).check(matches(CustomEditText.checkValidation(R.string.empty_username)))
    }

    @Test
    fun check_if_login_got_password_validation() {
        onView(withId(R.id.layout_login)).check(matches(isDisplayed()))
        onView(withId(R.id.et_username)).perform(CustomEditText.setText("asdf@gmail.com"))
        onView(withId(R.id.btn_login)).perform(ViewActions.click())
        onView(withId(R.id.et_password)).check(matches(CustomEditText.checkValidation(R.string.invalid_password)))
    }

    @Test
    fun check_if_login_navigate_to_register_successfully() {
        onView(withId(R.id.layout_login)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_no_account_yet)).perform(ViewActions.click())
        onView(withId(R.id.layout_register)).check(matches(isDisplayed()))
    }
}
