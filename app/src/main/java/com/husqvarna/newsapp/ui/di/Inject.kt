package com.husqvarna.newsapp.ui.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.husqvarna.newsapp.api.APIEndPoints
import com.husqvarna.newsapp.data.GetNewsRxRepositoryImpl
import com.husqvarna.newsapp.extras.Configuration
import com.husqvarna.newsapp.ui.home.HomeViewModelFactory
import com.husqvarna.newsapp.data.datasource.NewsDataSource

object Inject {

    fun provideLocale(): String = "in"
    fun provideSources(): String = "techcrunch"

    fun provideHomeViewModel(context: Context):
            ViewModelProvider.Factory {

        val pagingSource =
            NewsDataSource(
                apiEndPoints = APIEndPoints.create(),
                apiKey = Configuration.NEWS_API_KEY,
                value = provideSources()
//                country = provideLocale()
            )

        val repository =
            GetNewsRxRepositoryImpl(
                pagingSource = pagingSource
            )

        return HomeViewModelFactory(
            repository
        )
    }
}