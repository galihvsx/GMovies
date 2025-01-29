package com.gtools.gmovies.favorite.di

import com.gtools.gmovies.favorite.ui.FavoriteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favoriteModule = module {
    viewModel {
        FavoriteViewModel(get())
    }
}