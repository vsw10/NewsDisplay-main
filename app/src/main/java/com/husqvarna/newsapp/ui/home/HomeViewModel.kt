package com.husqvarna.newsapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.rxjava2.cachedIn
import com.husqvarna.newsapp.data.GetNewsRxRepository
import com.husqvarna.newsapp.data.dataresponse.NewsResponse
import io.reactivex.Flowable


class HomeViewModel(private val repository: GetNewsRxRepository) : ViewModel() {

    fun newsList(): Flowable<PagingData<NewsResponse.NewsTop>> {
        return repository.getNews()
            .map { pagingData ->
                pagingData.filter {
                    it.author != null
                }
            }
            .cachedIn(viewModelScope)
    }

    var topNewsList: Array<String>? = null
    var pagingData: PagingData<NewsResponse.NewsTop>? = null
}