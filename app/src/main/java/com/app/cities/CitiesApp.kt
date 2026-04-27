package com.app.cities

import android.app.Application
import com.app.cities.di.AppContainer

class CitiesApp : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}