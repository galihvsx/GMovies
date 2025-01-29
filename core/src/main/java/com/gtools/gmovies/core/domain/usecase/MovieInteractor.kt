package com.gtools.gmovies.core.domain.usecase

import com.gtools.gmovies.core.data.Resource
import com.gtools.gmovies.core.domain.model.Movie
import com.gtools.gmovies.core.domain.repository.IMovieRepository
import kotlinx.coroutines.flow.Flow

class MovieInteractor(private val iMovieRepository: IMovieRepository) : MovieUseCase {

    override fun getAllMovies(sort: String): Flow<Resource<List<Movie>>> =
        iMovieRepository.getAllMovies(sort)


    override fun getFavoriteMovies(sort: String): Flow<List<Movie>> =
        iMovieRepository.getFavoriteMovies(sort)

    override fun getSearchMovies(search: String): Flow<List<Movie>> =
        iMovieRepository.getSearchMovies(search)

    override fun setMovieFavorite(movie: Movie, state: Boolean) =
        iMovieRepository.setMovieFavorite(movie, state)

}