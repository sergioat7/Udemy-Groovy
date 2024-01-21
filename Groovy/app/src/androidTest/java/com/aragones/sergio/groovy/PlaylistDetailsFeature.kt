package com.aragones.sergio.groovy

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertNotExist
import com.aragones.sergio.groovy.playlist.di.idlingResource
import org.hamcrest.CoreMatchers
import org.junit.Test

class PlaylistDetailsFeature : BaseUITest() {

    @Test
    fun displaysPlaylistNameAndDetails() {

        navigateToPlaylistDetails(0)

        assertDisplayed("Hard Rock Cafe")

        assertDisplayed("Rock your senses with this timeless signature vibe list. \n\n • Poison \n • You shook me all night \n • Zombie \n • Rock'n Me \n • Thunderstruck \n • I Hate Myself for Loving you \n • Crazy \n • Knockin' on Heavens Door")
    }

    @Test
    fun displaysLoaderWhileFetchingThePlaylistDetails() {

        navigateToPlaylistDetails(0)

        IdlingRegistry.getInstance().unregister(idlingResource.asAndroidX())
        mActivityRule.scenario.onActivity {
            assertDisplayed(R.id.details_loader)
        }
    }

    @Test
    fun hidesLoader() {

        navigateToPlaylistDetails(0)

        assertNotDisplayed(R.id.details_loader)
    }

    @Test
    fun displaysErrorMessageWhenNetworkFails() {

        navigateToPlaylistDetails(1)

        assertDisplayed(R.string.generic_error)
    }

    @Test
    fun hidesErrorMessage() {

        navigateToPlaylistDetails(1)

        Thread.sleep(3000)

        assertNotExist(R.string.generic_error)
    }

    private fun navigateToPlaylistDetails(row: Int) {

        onView(
            CoreMatchers.allOf(
                ViewMatchers.withId(R.id.playlist_image),
                ViewMatchers.isDescendantOfA(
                    nthChildOf(
                        ViewMatchers.withId(R.id.playlists_list),
                        row
                    )
                )
            )
        ).perform(ViewActions.click())
    }
}