package com.gtools.gmovies.di

import com.gtools.gmovies.core.domain.usecase.MovieInteractor
import com.gtools.gmovies.core.domain.usecase.MovieUseCase
import com.gtools.gmovies.ui.home.HomeViewModel
import com.gtools.gmovies.ui.movie_detail.MovieDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { MovieDetailViewModel(get()) }
}

val useCaseModule = module {
    factory<MovieUseCase> { MovieInteractor(get()) }
}