package com.husqvarna.newsapp

import android.app.Application
import com.husqvarna.newsapp.api.APIEndPoints
import com.husqvarna.newsapp.logs.NDLogs
import com.husqvarna.newsapp.ui.di.appModule
import com.husqvarna.newsapp.ui.di.networkModule
import com.husqvarna.newsapp.ui.di.viewModelModule
import io.realm.Realm
import io.realm.RealmConfiguration
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NewsApp : Application() {

    val TAG = "NewsApp"
    var apiEndPoints: APIEndPoints? = null

    override fun onCreate() {
        super.onCreate()

        initialise()
        initialiseRealm()
    }

    private fun initialise() {

        startKoin {
            androidContext(this@NewsApp)
            modules(
                listOf(
                    appModule, networkModule, viewModelModule
                )
            )
        }
    }

    fun initialiseRealm() {

        NDLogs.debug(TAG, " initialiseRealm ")
        Realm.init(this)

        Realm.setDefaultConfiguration(
            RealmConfiguration.Builder()
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build()
        )

    }
}