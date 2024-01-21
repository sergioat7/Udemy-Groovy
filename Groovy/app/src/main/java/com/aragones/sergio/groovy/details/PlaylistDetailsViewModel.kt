package com.aragones.sergio.groovy.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.aragones.sergio.groovy.details.PlaylistDetails
import kotlinx.coroutines.flow.onEach

class PlaylistDetailsViewModel(
    private val service: PlaylistDetailsService
) : ViewModel() {

    val loader = MutableLiveData<Boolean>()

    val playlistDetails: MutableLiveData<Result<PlaylistDetails>> = MutableLiveData()

    fun getPlaylistDetails(id: String) {
        viewModelScope.launch {

            loader.postValue(true)
            service.fetchPlaylistDetails(id)
                .onEach {
                    loader.postValue(false)
                }
                .collect {
                    playlistDetails.postValue(it)
                }
        }
    }
}
