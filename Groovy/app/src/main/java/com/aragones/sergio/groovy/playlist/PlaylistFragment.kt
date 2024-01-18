package com.aragones.sergio.groovy.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aragones.sergio.groovy.databinding.FragmentPlaylistBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlaylistFragment : Fragment() {

    private var binding: FragmentPlaylistBinding? = null

    @Inject
    lateinit var viewModelFactory: PlaylistViewModelFactory

    private val viewModel: PlaylistViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        val view = binding?.root

        viewModel.loader.observe(this as LifecycleOwner) { loading ->

            when (loading) {
                true -> binding?.loader?.visibility = View.VISIBLE
                else -> binding?.loader?.visibility = View.GONE
            }
        }

        viewModel.playlists.observe(this as LifecycleOwner) { playlists ->

            if (playlists.getOrNull() != null)
                setupList(binding?.playlistsList, requireNotNull(playlists.getOrNull()))
            else {
                //TODO
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
    }

    private fun setupList(
        view: View?,
        playlists: List<Playlist>
    ) {
        with(view as RecyclerView) {

            layoutManager = LinearLayoutManager(context)
            adapter = MyPlaylistRecyclerViewAdapter(playlists) { id ->

                val action =
                    PlaylistFragmentDirections.actionPlaylistFragmentToPlaylistDetailFragment(id)
                findNavController().navigate(action)
            }
        }
    }
}