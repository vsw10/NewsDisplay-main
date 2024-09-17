package com.husqvarna.newsapp.ui.di

import com.husqvarna.newsapp.extras.Configuration
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    single(named("NEWS_API_KEY")) {
        provideNewsApiKey()
    }

    single(named("BASE_URL_NEWS")) {
        getBaseUrl()
    }

    single(named("GET_LOCALE")) {
        getLocale()
    }
}

fun provideNewsApiKey() = Configuration.NEWS_API_KEY
fun getBaseUrl() = Configuration.BASE_URL_NEWS
fun getLocale() = Configuration.COUNTRY_US