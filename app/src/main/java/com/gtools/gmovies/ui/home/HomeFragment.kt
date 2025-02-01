package com.gtools.gmovies.ui.home

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gtools.gmovies.core.data.Resource
import com.gtools.gmovies.core.domain.model.Movie
import com.gtools.gmovies.core.ui.MoviesAdapter
import com.gtools.gmovies.core.ui.SearchResultAdapter
import com.gtools.gmovies.core.utils.SortUtils
import com.gtools.gmovies.databinding.FragmentHomeBinding
import com.gtools.gmovies.ui.movie_detail.MovieDetailActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModel()
    private var moviesAdapter: MoviesAdapter? = null
    private var searchAdapter: SearchResultAdapter? = null
    private var sorter = SortUtils.RANDOM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val backCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.searchView.isShown) {
                    binding.searchView.hide()
                } else {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Exit")
                        .setMessage("Are you sure you want to exit?")
                        .setPositiveButton("Yes") { _, _ ->
                            requireActivity().finish()
                        }
                        .setNegativeButton("No", null)
                        .show()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backCallback)

        setupAppBar()

        moviesAdapter = MoviesAdapter()
        searchAdapter = SearchResultAdapter()
        pushList(sorter)
        setupRv()
        setupSearchView()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        moviesAdapter = null
        searchAdapter = null
    }

    private fun setupSearchView() {
        val searchBar = binding.searchBar
        val searchView = binding.searchView
        val searchSuggestionRv = binding.searchSuggestionsRv

        searchSuggestionRv.layoutManager = LinearLayoutManager(context)
        searchSuggestionRv.adapter = searchAdapter

        searchAdapter?.onItemClick = { selectedData ->
            val intent = Intent(requireContext(), MovieDetailActivity::class.java)
            intent.putExtra("EXTRA_MOVIE", selectedData)
            startActivity(intent)
        }

        searchBar.setOnClickListener {
            searchView.show()
        }

        searchView.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }

            override fun afterTextChanged(p0: Editable?) {
                Log.d("SearchView", "afterTextChanged: $p0")
                pushSearchResult(p0.toString())
            }
        })
    }

    private fun setupAppBar() {
        val toolBar = binding.toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolBar)
        val collapsingToolbar = binding.collapsingToolbar
        collapsingToolbar.expandedTitleTextSize = 0f
    }

    private fun setupRv() {
        val movieListRv = binding.moviesRv
        with(movieListRv) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = moviesAdapter
        }

        moviesAdapter?.onItemClick = { selectedData ->
            val intent = Intent(requireContext(), MovieDetailActivity::class.java)
            intent.putExtra("EXTRA_MOVIE", selectedData)
            startActivity(intent)
        }
    }

    private val searchResultsObserver = Observer<List<Movie>> { movies ->
        Log.d("SearchResultsObserver", "searchResultsObserver: $movies")
        searchAdapter?.setData(movies)
    }

    private val moviesObserver = Observer<Resource<List<Movie>>> { movies ->
        when (movies) {
            is Resource.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            is Resource.Success -> {
                binding.progressBar.visibility = View.GONE
                Log.d("MoviesObserver", "moviesObserver: ${movies.data}")
                moviesAdapter?.setData(movies.data)
            }
            is Resource.Error -> {
                binding.progressBar.visibility = View.GONE
                Log.e("MoviesObserver", "Error: ${movies.data}")
                Toast.makeText(context, "Terjadi kesalahan: ${movies.data}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun pushList(sort: String) {
        viewModel.getMovies(sort).observe(viewLifecycleOwner, moviesObserver)
    }

    private fun pushSearchResult(query: String) {
        viewModel.searchMovies(query).observe(viewLifecycleOwner, searchResultsObserver)
    }
}