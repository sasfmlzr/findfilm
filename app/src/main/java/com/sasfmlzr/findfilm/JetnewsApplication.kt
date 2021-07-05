package com.sasfmlzr.findfilm

import android.app.Application

class JetnewsApplication : Application() {

    // AppContainer instance used by the rest of classes to obtain dependencies
    //lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
       // container = AppContainerImpl(this)
    }
}
