package com.franzandel.dicodingintermediatesubmission.ui.splashscreen

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.data.local.serializer.settingsDataStore
import com.franzandel.dicodingintermediatesubmission.test.EspressoIdlingResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
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
class SplashScreenActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        ActivityScenario.launch(SplashScreenActivity::class.java)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun check_if_navigated_to_home() {
        val context: Context = ApplicationProvider.getApplicationContext()
        runBlocking {
            context.settingsDataStore.updateData { settings ->
                settings.toBuilder()
                    .setToken("fakeToken")
                    .build()
            }
        }
        onView(withId(R.id.srl_home)).check(matches(isDisplayed()))
    }

    @Test
    fun check_if_navigated_to_login() {
        val context: Context = ApplicationProvider.getApplicationContext()
        runBlocking {
            context.settingsDataStore.updateData { settings ->
                settings.toBuilder()
                    .setToken("")
                    .build()
            }
        }
        onView(withId(R.id.layout_login)).check(matches(isDisplayed()))
    }
}
