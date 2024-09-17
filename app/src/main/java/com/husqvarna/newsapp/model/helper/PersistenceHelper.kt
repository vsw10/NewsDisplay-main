package com.husqvarna.newsapp.model.helper

import com.husqvarna.newsapp.Util.utility
import com.husqvarna.newsapp.logs.NDLogs
import com.husqvarna.newsapp.model.NewsArticleDetails
import com.husqvarna.newsapp.data.dataresponse.NewsResponse
import io.realm.Realm
import io.realm.RealmObject
import io.realm.kotlin.createObject

object PersistenceHelper {

    private val pendingObjects: MutableList<RealmObject> =
        mutableListOf()

    fun saveResult(
        obj: RealmObject, newsTop: NewsResponse.NewsTop?,
        onSuccess: Realm.Transaction.OnSuccess? = null,
        onError: Realm.Transaction.OnError? = null
    ) {
        synchronized(pendingObjects) {
            pendingObjects.add(obj)
        }
        process(newsTop, onSuccess, onError)
    }

    private fun process(
        newsTop: NewsResponse.NewsTop?,
        onSuccess: Realm.Transaction.OnSuccess? = null,
        onError: Realm.Transaction.OnError? = null
    ) {

        Realm.getDefaultInstance().use {
            it.executeTransactionAsync(
                object : Realm.Transaction {
                    override fun execute(realm: Realm) {
                        NDLogs.debug(
                            "PH",
                            " Execute Realm Transactions"
                        )
                        var nextId: Int? =
                            (realm.where(NewsArticleDetails::class.java).max("id")?.toInt())
                        if (nextId == null) {
                            nextId = 1
                        } else {
                            nextId =
                                (realm.where(NewsArticleDetails::class.java).max("id")?.toInt()
                                    ?.plus(1))
                        }

                        val newsDetails = realm.createObject<NewsArticleDetails>(nextId)
                        var sourceValue = utility.splitSourceString(newsTop?.source.toString())
                        newsDetails.mSource = sourceValue
                        newsDetails.mTitle = newsTop?.title
                        newsDetails.image = newsTop?.urlToImage
                        newsDetails.url = newsTop?.url
                        newsDetails.mContent = newsTop?.content
                        newsDetails.isBookmarked = 1
                        realm.copyToRealmOrUpdate(newsDetails)
                    }
                }, onSuccess, onError
            )
        }
    }
}