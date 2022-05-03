package com.franzandel.dicodingintermediatesubmission

import android.app.Application
import android.content.Context

/**
 * Created by Franz Andel
 * on 03 May 2022.
 */

class ThisApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {
        lateinit var appContext: Context
    }
}
