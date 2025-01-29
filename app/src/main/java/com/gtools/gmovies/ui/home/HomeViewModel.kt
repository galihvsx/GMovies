package com.gtools.gmovies.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.gtools.gmovies.core.data.Resource
import com.gtools.gmovies.core.domain.model.Movie
import com.gtools.gmovies.core.domain.usecase.MovieUseCase

class HomeViewModel(private val movieUseCase: MovieUseCase): ViewModel() {
    fun getMovies(sort: String): LiveData<Resource<List<Movie>>> {
        return movieUseCase.getAllMovies(sort).asLiveData()
    }

    fun searchMovies(query: String): LiveData<List<Movie>> {
        return movieUseCase.getSearchMovies(query).asLiveData()
    }
}