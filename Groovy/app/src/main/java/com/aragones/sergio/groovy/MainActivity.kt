package com.aragones.sergio.groovy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aragones.sergio.groovy.playlist.PlaylistFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
    }
}