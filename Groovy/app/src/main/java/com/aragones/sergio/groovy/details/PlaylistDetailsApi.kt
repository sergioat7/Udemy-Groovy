package com.aragones.sergio.groovy.details

import com.aragones.sergio.groovy.details.PlaylistDetails
import retrofit2.http.GET
import retrofit2.http.Path

interface PlaylistDetailsApi {

    @GET("playlists-details/{playlistId}")
    suspend fun fetchPlaylistDetails(@Path(value = "playlistId") playlistId: String): PlaylistDetails
}