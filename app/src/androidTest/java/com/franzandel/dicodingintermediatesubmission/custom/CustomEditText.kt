package com.franzandel.dicodingintermediatesubmission.custom

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.franzandel.dicodingintermediatesubmission.ui.customview.CustomEditText
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf


/**
 * Created by Franz Andel
 * on 30 August 2022.
 */

object CustomEditText {
    fun setText(text: String): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return allOf(isDisplayed(), isAssignableFrom(CustomEditText::class.java))
            }

            override fun perform(uiController: UiController, view: View) {
                (view as CustomEditText).setText(text)
            }

            override fun getDescription(): String {
                return "Click on custom edit text"
            }
        }
    }

    fun checkValidation(message: Int): BoundedMatcher<View?, CustomEditText> {
        return object : BoundedMatcher<View?, CustomEditText>(CustomEditText::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("Checking the matcher on received view: ")
                description.appendText("with message=$message")
            }

            override fun matchesSafely(foundView: CustomEditText): Boolean {
                return foundView.error == foundView.context.getString(message)
            }
        }
    }
}
