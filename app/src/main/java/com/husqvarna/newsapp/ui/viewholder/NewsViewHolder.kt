package com.husqvarna.newsapp.ui.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.husqvarna.newsapp.Util.utility
import com.husqvarna.newsapp.logs.NDLogs
import com.husqvarna.newsapp.model.NewsArticleDetails
import com.husqvarna.newsapp.R
import com.husqvarna.newsapp.data.dataresponse.NewsResponse
import com.husqvarna.newsapp.databinding.ListItemsBinding

class NewsViewHolder(private val listItemsBinding: ListItemsBinding) :
    RecyclerView.ViewHolder(listItemsBinding.root) {
    fun bind(news: NewsResponse.NewsTop) {
        with(news) {
            listItemsBinding.popularNewsTitle.text = title
            listItemsBinding.popularNewsHeadline.text = content

            listItemsBinding.newsSource.text =
                utility.splitSourceString(source.toString())
            listItemsBinding.popularNewsImage.load(urlToImage) {
                crossfade(true)
            }
        }
    }

    fun bindDb(newsArticleDetails: NewsArticleDetails) {
        with(newsArticleDetails) {
            listItemsBinding.popularNewsTitle.text = newsArticleDetails.mTitle
            listItemsBinding.popularNewsHeadline.text =
                newsArticleDetails.mContent

            listItemsBinding.newsSource.text =
                newsArticleDetails.mSource
            listItemsBinding.popularNewsImage.load(newsArticleDetails.image) {
                crossfade(true)
            }
        }
    }

    companion object {
        fun create(
            viewGroup: ViewGroup
        ): NewsViewHolder {

            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.list_items, viewGroup, false)

            val binding = ListItemsBinding.bind(view)
            binding.newsBookmarked.setOnClickListener {

                NDLogs.debug(
                    "NewsViewHolder",
                    " News Bookmarked Clicked ${binding.popularNewsHeadline.text}"
                )
            }

            val newsViewHolder = NewsViewHolder(binding)

            /*RxView.clicks(view)
                .takeUntil(RxView.detaches(newsViewHolder.itemView))
                .subscribe{

                }*/
            return newsViewHolder
        }
    }
}