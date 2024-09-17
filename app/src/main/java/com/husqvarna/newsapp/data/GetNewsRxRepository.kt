package com.husqvarna.newsapp.data

import androidx.paging.PagingData
import com.husqvarna.newsapp.data.dataresponse.NewsResponse
import io.reactivex.Flowable

interface GetNewsRxRepository {

    fun getNews(): Flowable<PagingData<NewsResponse.NewsTop>>
}