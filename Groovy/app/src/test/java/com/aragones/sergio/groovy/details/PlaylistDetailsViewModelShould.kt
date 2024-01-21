package com.aragones.sergio.groovy.details

import com.aragones.sergio.groovy.utils.BaseUnitTest
import com.aragones.sergio.groovy.utils.getValueForTest
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import com.aragones.sergio.groovy.utils.captureValues
import org.junit.Assert

class PlaylistDetailsViewModelShould : BaseUnitTest() {

    private lateinit var viewModel: PlaylistDetailsViewModel
    private val id = "1"
    private val service: PlaylistDetailsService = mock()
    private val playlistDetails: PlaylistDetails = mock()
    private val expected = Result.success(playlistDetails)
    private val exception = RuntimeException("Something went wrong")
    private val error = Result.failure<PlaylistDetails>(exception)

    @Test
    fun getPlaylistDetailsFromService() = runBlockingTest {

        mockSuccessfulCase()

        viewModel.getPlaylistDetails(id)

        viewModel.playlistDetails.getValueForTest()

        verify(service, times(1)).fetchPlaylistDetails(id)
    }

    @Test
    fun emitPlaylistDetailsFromService() = runBlockingTest {

        mockSuccessfulCase()

        viewModel.getPlaylistDetails(id)

        assertEquals(expected, viewModel.playlistDetails.getValueForTest())
    }

    @Test
    fun emitErrorWhenServiceFails() = runBlockingTest {

        mockErrorCase()

        viewModel.getPlaylistDetails(id)

        assertEquals(error, viewModel.playlistDetails.getValueForTest())
    }

    @Test
    fun showLoaderWhileLoading() = runBlockingTest {

        mockSuccessfulCase()

        viewModel.loader.captureValues {

            viewModel.getPlaylistDetails(id)
            viewModel.playlistDetails.getValueForTest()

            Assert.assertEquals(true, values[0])
        }
    }

    @Test
    fun closeLoaderAfterPlaylistsLoad() = runBlockingTest {

        mockSuccessfulCase()

        viewModel.loader.captureValues {

            viewModel.getPlaylistDetails(id)
            viewModel.playlistDetails.getValueForTest()

            Assert.assertEquals(false, values.last())
        }
    }

    @Test
    fun closeLoaderAfterError() = runBlockingTest {

        mockErrorCase()

        viewModel.loader.captureValues {

            viewModel.getPlaylistDetails(id)
            viewModel.playlistDetails.getValueForTest()

            Assert.assertEquals(false, values.last())
        }
    }

    private suspend fun mockSuccessfulCase() {

        whenever(service.fetchPlaylistDetails(id)).thenReturn(
            flow {
                emit(expected)
            }
        )

        viewModel = PlaylistDetailsViewModel(service)
    }

    private suspend fun mockErrorCase() {

        whenever(service.fetchPlaylistDetails(id)).thenReturn(
            flow {
                emit(error)
            }
        )

        viewModel = PlaylistDetailsViewModel(service)
    }
}