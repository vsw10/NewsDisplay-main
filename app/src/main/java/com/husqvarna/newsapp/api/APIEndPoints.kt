package com.husqvarna.newsapp.api

import com.husqvarna.newsapp.extras.Configuration
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.husqvarna.newsapp.data.dataresponse.NewsResponse
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface APIEndPoints {

    /* @GET(APIEndPointsConstant.TOP_HEADLINES)
     fun topHeadlines(@Query("apiKey") apiKey: String?,
                      @Query("category") category: String?,
                      @Query("language") language: String?,
                      @Query("country") country: String?) : Single<NewsResponse>*/

    @GET(APIEndPointsConstant.TOP_HEADLINES)
    fun topHeadlines(
        @Query("country") country: String?,
        @Query("apiKey") apiKey: String?,
    ): Single<NewsResponse>


    companion object {

        fun create(): APIEndPoints {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(Configuration.BASE_URL_NEWS)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(APIEndPoints::class.java)
        }

        val responseNewsSubject =
            PublishSubject.create<Pair<List<NewsResponse.NewsTop>?, Throwable>>()
        val responseNewsObservable: Observable<Pair<List<NewsResponse.NewsTop>?, Throwable>>
            get() = responseNewsSubject.share()
    }
}