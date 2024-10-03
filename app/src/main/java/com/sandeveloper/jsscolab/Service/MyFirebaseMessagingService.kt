package com.sandeveloper.jsscolab.Service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.domain.Interfaces.ProfileRepository
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Workers.FcmTokenWorker
import com.sandeveloper.jsscolab.presentation.Main.ProfileViewModel
import com.sandeveloper.jsscolab.presentation.Notification
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var profileRepository: ProfileRepository
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.tag("FCM Token").d("New token: " + token)
        val inputData = Data.Builder()
            .putString("fcmToken", token)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<FcmTokenWorker>()
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(workRequest)
        }
        // Update the new token to your server

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            sendNotification(it.title, it.body)
        }
    }

    private fun sendNotification(title: String?, messageBody: String?) {
        val intent = Intent(this, Notification::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(this, "default")
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.logogradhd )
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }
}
