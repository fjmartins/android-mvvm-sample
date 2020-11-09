package com.fjmartins.forexrates

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.fjmartins.forexrates.view.MainActivity
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ForexRatesInstrumentedUnitTest {

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.fjmartins.forexrates", appContext.packageName)
    }

    @Test
    fun currencySelectionFragmentDisplayedTest() {
        // Set up
        ActivityScenario.launch(MainActivity::class.java)

        // Verify
        onView(withId(R.id.fragment_currency_parent)).check(matches(isDisplayed()))
    }

    @Test
    fun navigateToRatesScreenTest() {
        // Set up
        ActivityScenario.launch(MainActivity::class.java)

        // Navigate to Rates Fragment
        onView(withId(R.id.viewRatesButton)).perform(click())

        // Verify
        onView(withId(R.id.fragment_rates_parent)).check(matches(isDisplayed()))
    }
}