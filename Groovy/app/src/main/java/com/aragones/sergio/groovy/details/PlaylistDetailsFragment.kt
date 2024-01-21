package com.aragones.sergio.groovy.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.navArgs
import com.aragones.sergio.groovy.R
import com.aragones.sergio.groovy.databinding.FragmentPlaylistDetailBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlaylistDetailsFragment : Fragment() {

    private var binding: FragmentPlaylistDetailBinding? = null

    @Inject
    lateinit var viewModelFactory: PlaylistDetailsViewModelFactory

    private val viewModel: PlaylistDetailsViewModel by viewModels {
        viewModelFactory
    }

    private val args: PlaylistDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPlaylistDetailBinding.inflate(inflater, container, false)
        val view = binding?.root
        val id = args.playlistId

        viewModel.getPlaylistDetails(id)
        observeLoader()
        observePlaylistDetails()

        return view
    }

    private fun observeLoader() {

        viewModel.loader.observe(this as LifecycleOwner) { loading ->

            when (loading) {
                true -> binding?.detailsLoader?.visibility = View.VISIBLE
                else -> binding?.detailsLoader?.visibility = View.GONE
            }
        }
    }

    private fun observePlaylistDetails() {

        viewModel.playlistDetails.observe(this as LifecycleOwner) { playlistDetails ->

            if (playlistDetails.getOrNull() != null) {
                setupUI(playlistDetails)
            } else {
                Snackbar.make(
                    binding!!.playlistsDetailsRoot,
                    R.string.generic_error,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun setupUI(playlistDetails: Result<PlaylistDetails>) {

        binding?.playlistName?.text = requireNotNull(playlistDetails.getOrNull()).name
        binding?.playlistDetails?.text = requireNotNull(playlistDetails.getOrNull()).details
    }
}