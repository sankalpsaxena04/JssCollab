package com.sandeveloper.jsscolab.domain.HelperClasses

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.sandeveloper.MainActivity
import com.sandeveloper.jsscolab.R
import com.sandeveloper.jsscolab.domain.Constants.NotificationType
import com.sandeveloper.jsscolab.domain.Models.FCMNotification
import com.sandeveloper.jsscolab.presentation.Notification

object NotificationBuilderUtil {

    fun showNotification(context:Context,notification:FCMNotification){
        val channelId = "fcm1"
        val channelName = "FCM Channel"
        val notificationId = System.currentTimeMillis().toInt()
        val notificationBuilder: NotificationCompat.Builder
        val soundUri: Uri = Uri.parse("android.resource://${context.packageName}/raw/notification1")

        when(notification.notificationType){
            NotificationType.REQUEST_FAILED_NOTIFICATION -> {
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
                )
                notificationBuilder = NotificationCompat.Builder(context, channelId)
                    .setContentTitle(notification.title)
                    .setContentText(notification.message)
                    .setSmallIcon(R.drawable.logogradhd)
                    .setColor(Color.RED)
                    .setAutoCancel(true)
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText(notification.message)
                    )
                    .setSound(soundUri)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setContentIntent(pendingIntent)
            }

            NotificationType.CHAT_MESSAGE_NOTIFICATION -> {
                val intent = Intent(context,Notification::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra("type",NotificationType.CHAT_MESSAGE_NOTIFICATION.name)
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                notificationBuilder =NotificationCompat.Builder(context,channelId)
                    .setContentTitle(notification.title)
                    .setContentText(notification.message)
                    .setSmallIcon(R.drawable.logogradhd)
                    .setColor(Color.RED)
                    .setAutoCancel(false)
                    .setSound(soundUri)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(notification.message))
                    .setContentIntent(pendingIntent)

            }
            NotificationType.NEW_POST_NOTIFICATION -> {
                val intent = Intent(context,Notification::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra("type",NotificationType.NEW_POST_NOTIFICATION.name)
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                notificationBuilder =NotificationCompat.Builder(context,channelId)
                    .setContentTitle(notification.title)
                    .setContentText(notification.message)
                    .setSmallIcon(R.drawable.logogradhd)
                    .setColor(Color.RED)
                    .setAutoCancel(false)
                    .setSound(soundUri)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(notification.message))
                    .setContentIntent(pendingIntent)

            }
        }

        val notificationManager =context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            enableLights(true)
            lightColor = Color.RED
            enableVibration(true)
            setSound(soundUri, null)
        }


        // Create the notification channel
        notificationManager.createNotificationChannel(channel)

        // Show the notification
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}