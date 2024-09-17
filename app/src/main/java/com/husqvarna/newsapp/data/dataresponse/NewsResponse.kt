package com.husqvarna.newsapp.data.dataresponse

import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("totalResults") val total: Int = 0,
    val page: Int = 5,
    @SerializedName("articles") val results: List<NewsTop>
) {

    data class NewsTop(

        @SerializedName("source")
        val source: Any,

        @SerializedName("author")
        val author: String,

        @SerializedName("content")
        val content: String,

        @SerializedName("description")
        val description: String,

        @SerializedName("publishedAt")
        val publishedAt: String,

        @SerializedName("title")
        val title: String,

        @SerializedName("url")
        val url: String,

        @SerializedName("urlToImage")
        val urlToImage: String
    )

    data class Sources(
        @SerializedName("id")
        val id: String,
        @SerializedName("name")
        val name: String,
    )

}
