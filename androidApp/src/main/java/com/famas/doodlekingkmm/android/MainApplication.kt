package com.famas.doodlekingkmm.android

import android.app.Application
import com.famas.doodlekingkmm.di.getAllModules
import com.famas.doodlekingkmm.di.initKoin

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            modules(getAllModules())
        }
    }
}