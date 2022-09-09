package com.franzandel.dicodingintermediatesubmission.ui.detail

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.custom.lazyActivityScenarioRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test

/**
 * Created by Franz Andel
 * on 09 September 2022.
 */

@LargeTest
@HiltAndroidTest
class DetailActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val rule = lazyActivityScenarioRule<DetailActivity>(launchActivity = false)

    @Test
    fun check_if_detail_without_location_shown_correctly() {
        val fakeStoryDetail = StoryDetail(
            createdAt = "2022-08-18T21:32:30.752Z",
            description = "2",
            id = "story-tVyHngkIXympcInp",
            latitude = null,
            longitude = null,
            name = "Dstory",
            photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1660858350751_Valb0Cqa.jpg"
        )

        val intent = Intent(ApplicationProvider.getApplicationContext(), DetailActivity::class.java)
            .putExtra(DetailActivity.EXTRA_STORY_DETAIL, fakeStoryDetail)
        rule.launch(intent)

        onView(withId(R.id.tv_name)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_location)).check(matches(not(isDisplayed())))
        onView(withId(R.id.iv_detail)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_description)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_time)).check(matches(isDisplayed()))
    }

    @Test
    fun check_if_detail_with_location_shown_correctly() {
        val fakeStoryDetailWithLocation = StoryDetail(
            createdAt = "2022-08-18T21:32:30.752Z",
            description = "2",
            id = "story-tVyHngkIXympcInp",
            latitude = -6.4517064,
            longitude = 107.9145669,
            name = "Dstory",
            photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1660858350751_Valb0Cqa.jpg"
        )

        val intent = Intent(ApplicationProvider.getApplicationContext(), DetailActivity::class.java)
            .putExtra(DetailActivity.EXTRA_STORY_DETAIL, fakeStoryDetailWithLocation)
        rule.launch(intent)

        onView(withId(R.id.menu_map)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_name)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_location)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_detail)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_description)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_time)).check(matches(isDisplayed()))
    }
}
