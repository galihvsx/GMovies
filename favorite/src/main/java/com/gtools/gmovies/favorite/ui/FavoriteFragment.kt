package com.gtools.gmovies.favorite.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gtools.gmovies.core.domain.model.Movie
import com.gtools.gmovies.core.utils.SortUtils
import com.gtools.gmovies.favorite.databinding.FragmentFavoriteBinding
import com.gtools.gmovies.favorite.di.favoriteModule
import com.gtools.gmovies.ui.movie_detail.MovieDetailActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var favoriteAdapter: FavoriteAdapter
    private val viewModel: FavoriteViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        favoriteAdapter = FavoriteAdapter()
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        loadKoinModules(favoriteModule)
        setupAppBar()
        setupRvFavorite()
        pushList()
    }

    private fun setupRvFavorite() {
        with(binding.rvFavorite) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = favoriteAdapter
        }
        favoriteAdapter.onItemClick = { movie ->
            val intent = Intent(context,
                MovieDetailActivity::class.java)
            intent.putExtra("EXTRA_MOVIE", movie)
            startActivity(intent)
        }
    }

    private fun setupAppBar() {
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)
        binding.toolbar.title = "Favorite Movies"
    }

    private val moviesObserver = Observer<List<Movie>> { movies ->
        if (movies.isEmpty()) {
            setDataState(DataState.ERROR)
        } else {
            setDataState(DataState.SUCCESS)
        }
        favoriteAdapter.setData(movies)
    }

    private fun setDataState(state: DataState) {
        when (state) {
            DataState.ERROR -> {
                binding.progressBar.visibility = View.GONE
                binding.notFound.visibility = View.VISIBLE
            }
            DataState.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.notFound.visibility = View.GONE
            }
            DataState.SUCCESS -> {
                binding.progressBar.visibility = View.GONE
                binding.notFound.visibility = View.GONE
            }
        }
    }

    private fun pushList() {
        viewModel.getFavoriteMovies(SortUtils.RANDOM).observe(viewLifecycleOwner, moviesObserver)
    }

    enum class DataState {
        LOADING, SUCCESS, ERROR
    }
}