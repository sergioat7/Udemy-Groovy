package com.aragones.sergio.groovy.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aragones.sergio.groovy.details.PlaylistDetailsViewModel
import javax.inject.Inject

class PlaylistDetailsViewModelFactory @Inject constructor(
    private val service: PlaylistDetailsService
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlaylistDetailsViewModel(service) as T
    }
}
