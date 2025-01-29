package com.gtools.gmovies.favorite.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.gtools.gmovies.core.domain.model.Movie
import com.gtools.gmovies.core.domain.usecase.MovieUseCase

class FavoriteViewModel(
    private val movieAppUseCase: MovieUseCase
): ViewModel() {
    fun getFavoriteMovies(sort: String): LiveData<List<Movie>> =
        movieAppUseCase.getFavoriteMovies(sort).asLiveData()
}