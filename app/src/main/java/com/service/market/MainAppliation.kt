package com.service.market

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MainApplication : Application() {

    private val MAPKIT_API_KEY = " "


    override fun onCreate() {
        super.onCreate()
        // Set the api key before calling initialize on MapKitFactory.


    }

}