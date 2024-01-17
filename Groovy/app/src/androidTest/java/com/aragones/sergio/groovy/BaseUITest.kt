package com.aragones.sergio.groovy

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aragones.sergio.groovy.playlist.di.idlingResource
import com.jakewharton.espresso.OkHttp3IdlingResource
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import android.support.test.espresso.IdlingResource as SupportIdlingResource
import android.support.test.espresso.IdlingResource.ResourceCallback as SupportResourceCallback
import androidx.test.espresso.IdlingResource as AndroidXIdlingResource
import androidx.test.espresso.IdlingResource.ResourceCallback as AndroidXResourceCallback

@RunWith(AndroidJUnit4::class)
abstract class BaseUITest {

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(idlingResource.asAndroidX())
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(idlingResource.asAndroidX())
    }

    fun nthChildOf(parentMatcher: Matcher<View>, childPosition: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("position $childPosition of parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                if (view.parent !is ViewGroup) return false
                val parent = view.parent as ViewGroup

                return (parentMatcher.matches(parent)
                        && parent.childCount > childPosition
                        && parent.getChildAt(childPosition) == view)
            }
        }
    }
}

/**
 * Allows [SupportIdlingResource] and [AndroidXIdlingResource] to exist in the same module.
 *
 * This will be needed until [OkHttp3IdlingResource] is migrated to AndroidX.
 */
internal fun SupportIdlingResource.asAndroidX(): AndroidXIdlingResource =
    AndroidXIdlingResource(this)

class AndroidXIdlingResource(private val delegate: SupportIdlingResource) : AndroidXIdlingResource {

    override fun getName(): String = delegate.name

    override fun isIdleNow(): Boolean = delegate.isIdleNow

    override fun registerIdleTransitionCallback(callback: AndroidXResourceCallback) = delegate
        .registerIdleTransitionCallback(callback.asSupport())
}

class SupportResourceCallback(private val delegate: AndroidXResourceCallback) :
    SupportResourceCallback {
    override fun onTransitionToIdle() = delegate.onTransitionToIdle()

}

internal fun AndroidXResourceCallback.asSupport(): SupportResourceCallback =
    SupportResourceCallback(this)