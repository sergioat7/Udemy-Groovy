package com.aragones.sergio.groovy.playlist

import com.aragones.sergio.groovy.utils.BaseUnitTest
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.lang.RuntimeException

class PlaylistServiceShould : BaseUnitTest() {

    private val api: PlaylistApi = mock()
    private lateinit var service: PlaylistService
    private val playlists: List<Playlist> = mock()

    @Test
    fun fetchPlaylistsFromApi() = runBlockingTest {

        service = PlaylistService(api)

        service.fetchPlaylists().first()

        verify(api, times(1)).fetchAllPlaylists()
    }

    @Test
    fun convertValuesToFlowResultAndEmitsThem() = runBlockingTest {

        service = PlaylistService(api)
        whenever(api.fetchAllPlaylists()).thenReturn(playlists)

        assertEquals(Result.success(playlists), service.fetchPlaylists().first())
    }

    @Test
    fun emitsErrorResultWhenNetworkFails() = runBlockingTest {

        service = PlaylistService(api)
        whenever(api.fetchAllPlaylists()).thenThrow(RuntimeException("Damn backend developers"))

        assertEquals("Something went wrong", service.fetchPlaylists().first().exceptionOrNull()?.message)
    }
}