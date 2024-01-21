package com.aragones.sergio.groovy.details

import com.aragones.sergio.groovy.utils.BaseUnitTest
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import com.aragones.sergio.groovy.details.PlaylistDetails

class PlaylistDetailsServiceShould : BaseUnitTest() {

    private lateinit var service: PlaylistDetailsService
    private val id = "100"
    private val api: PlaylistDetailsApi = mock()
    private val playlistDetails: PlaylistDetails = mock()
    private val exception = RuntimeException("Damn backend developers again 500!!!")

    @Test
    fun fetchPlaylistDetailsFromAPI() = runBlockingTest {

        mockSuccessfulCase()

        service.fetchPlaylistDetails(id).single()

        verify(api, times(1)).fetchPlaylistDetails(id)
    }

    @Test
    fun convertValuesToFlowResultAndEmitThem() = runBlockingTest {

        mockSuccessfulCase()

        assertEquals(Result.success(playlistDetails), service.fetchPlaylistDetails(id).single())
    }

    @Test
    fun emitErrorResultWhenNetworkFails() = runBlockingTest {

        mockErrorCase()

        assertEquals(
            "Something went wrong",
            service.fetchPlaylistDetails(id).single().exceptionOrNull()?.message
        )
    }

    private suspend fun mockErrorCase() {

        whenever(api.fetchPlaylistDetails(id)).thenThrow(exception)
        service = PlaylistDetailsService(api)
    }


    private suspend fun mockSuccessfulCase() {

        whenever(api.fetchPlaylistDetails(id)).thenReturn(playlistDetails)
        service = PlaylistDetailsService(api)
    }
}