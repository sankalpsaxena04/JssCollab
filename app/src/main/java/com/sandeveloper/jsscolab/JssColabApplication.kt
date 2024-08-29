package com.sandeveloper.jsscolab

import android.app.Application
import com.sandeveloper.jsscolab.domain.HelperClasses.PrefManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class JssColabApplication:Application() {

    override fun onCreate() {
        super.onCreate()

        PrefManager.initialize(this@JssColabApplication)

    }
}