package com.husqvarna.newsapp.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class NewsArticleDetails : RealmObject() {

    @PrimaryKey
    var id: Int? = null

    var mTitle: String? = null
    var mContent: String? = null
    var mSource: String? = null
    var image: String? = null
    var url: String? = null

    var isBookmarked: Int? = null
}