package com.example.beenthere

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before // called everytime a Test is called
    fun setUp() {
        ActivityScenario.launch(MainActivity::class.java)
    }

    // UI test?
    @Test
    fun checkCategoryPageRecyclerViewIsLoaded() {

        onView(withId(R.id.homeFragment)).check(matches(isDisplayed()))

        onView(withText("Life Meaning")).perform(click())


//        onView(withId(R.id.categoryFragment)).check(matches(isDisplayed()))

        Thread.sleep(1500)

        onView(withId(R.id.recycler_carouse)).check(matches(isDisplayed()))



    }
}