package com.franzandel.dicodingintermediatesubmission

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by Franz Andel
 * on 03 May 2022.
 */

@HiltAndroidApp
class ThisApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {
        lateinit var appContext: Context
    }
}
