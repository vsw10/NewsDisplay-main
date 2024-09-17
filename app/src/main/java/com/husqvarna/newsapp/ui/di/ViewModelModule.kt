package com.husqvarna.newsapp.ui.di

import com.husqvarna.newsapp.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule = module {

    viewModel(named("HOME_VIEW_MODEL")) {
        HomeViewModel(repository = get())
    }
}