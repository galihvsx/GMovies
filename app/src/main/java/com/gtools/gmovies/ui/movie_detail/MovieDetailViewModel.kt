package com.gtools.gmovies.ui.movie_detail

import androidx.lifecycle.ViewModel
import com.gtools.gmovies.core.domain.model.Movie
import com.gtools.gmovies.core.domain.usecase.MovieUseCase

class MovieDetailViewModel(private val movieAppUseCase: MovieUseCase) :
    ViewModel() {
    fun setFavoriteMovie(movie: Movie, newStatus: Boolean) {
        movieAppUseCase.setMovieFavorite(movie, newStatus)
    }
}