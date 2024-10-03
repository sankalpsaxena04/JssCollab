package com.sandeveloper.jsscolab

import android.app.Application
import com.google.firebase.FirebaseApp
import com.sandeveloper.jsscolab.domain.HelperClasses.PrefManager
import dagger.hilt.android.HiltAndroidApp
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

@HiltAndroidApp
class JssColabApplication:Application() {
    private lateinit var mSocket: Socket

    override fun onCreate() {
        super.onCreate()

        PrefManager.initialize(this@JssColabApplication)
        FirebaseApp.initializeApp(this)
        try {
            val opts = IO.Options()
            opts.query = PrefManager.getToken() // Optionally pass token here if needed for initial connection
            mSocket = IO.socket(BuildConfig.BASE_URL, opts)
            mSocket.connect()
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }
    fun getSocket(): Socket {
        return mSocket
    }
}