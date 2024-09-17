package com.husqvarna.newsapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.husqvarna.newsapp.data.GetNewsRxRepository

class HomeViewModelFactory(private val repository: GetNewsRxRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(
                repository
            ) as T
        }
        throw IllegalArgumentException("View Model ERROR")
    }

}