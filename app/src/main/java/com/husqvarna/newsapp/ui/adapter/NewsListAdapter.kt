package com.husqvarna.newsapp.ui.adapter

import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.husqvarna.newsapp.extras.Extras
import com.husqvarna.newsapp.logs.NDLogs
import com.husqvarna.newsapp.model.NewsArticleDetails
import com.husqvarna.newsapp.model.helper.PersistenceHelper
import com.husqvarna.newsapp.ui.viewholder.NewsViewHolder
import com.husqvarna.newsapp.R
import com.husqvarna.newsapp.data.dataresponse.NewsResponse
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class NewsListAdapter :
    PagingDataAdapter<NewsResponse.NewsTop,
            NewsViewHolder>(NewsDifferentiator) {

    val TAG = "NewsListAdapter"
    val itemClick = PublishSubject.create<String>()
    val itemClickObserver: Observable<String>
        get() = itemClick.share()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            NewsViewHolder {

        NDLogs.debug(TAG, " onCreateViewHolder ")
        val newsViewHolder = NewsViewHolder.create(
            parent
        )
        return newsViewHolder
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {

        NDLogs.debug(TAG, " onBindViewHolder ")

        getItem(position)?.let {
            holder.bind(it)
        }

        val bookmarkedImage: AppCompatImageView =
            holder.itemView.findViewById(R.id.news_bookmarked)
        bookmarkedImage.setOnClickListener {
            val newsTop = getItem(position)
            NDLogs.debug(TAG, " News Top position ${newsTop.toString()}")
            itemClick.onNext(Extras.BOOKMARKED)
            PersistenceHelper.saveResult(NewsArticleDetails(), newsTop = newsTop)
        }

    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        NDLogs.debug(TAG, " onAttachedToRecyclerView ")

        super.onAttachedToRecyclerView(recyclerView)
    }

    object NewsDifferentiator : DiffUtil.ItemCallback<NewsResponse.NewsTop>() {
        override fun areItemsTheSame(
            oldItem: NewsResponse.NewsTop,
            newItem: NewsResponse.NewsTop
        ): Boolean {
            NDLogs.debug("NewsDifferentiator", " areItemsTheSame ")
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(
            oldItem: NewsResponse.NewsTop,
            newItem: NewsResponse.NewsTop
        ): Boolean {
            NDLogs.debug("NewsDifferentiator", " areContentsTheSame ")
            return oldItem == newItem
        }
    }
}