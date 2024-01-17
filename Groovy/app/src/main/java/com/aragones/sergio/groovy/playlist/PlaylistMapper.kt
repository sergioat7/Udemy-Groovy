package com.aragones.sergio.groovy.playlist

import com.aragones.sergio.groovy.R
import javax.inject.Inject

class PlaylistMapper @Inject constructor(): Function1<List<PlaylistRaw>, List<Playlist>> {

    override fun invoke(playlistsRaw: List<PlaylistRaw>): List<Playlist> {
        return playlistsRaw.map {
            val image = when(it.category) {
                "rock" -> R.mipmap.rock
                else -> R.mipmap.playlist
            }

            Playlist(it.id, it.name, it.category, image)
        }
    }
}
