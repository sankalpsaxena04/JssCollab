package com.sandeveloper.jsscolab.domain.Utility

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.gson.JsonObject
import com.sandeveloper.jsscolab.domain.Constants.NotificationType
import com.sandeveloper.jsscolab.domain.Constants.Endpoints.Notifications as N
import com.sandeveloper.jsscolab.domain.Modules.NotificationEntity
import com.sandeveloper.jsscolab.domain.Workers.NotificationsFCMWorker
import java.util.concurrent.TimeUnit


object NotificationUtils {
    // TODO("""val projectTopic = "${Endpoints.defaultProject}_TOPIC_GENERAL"
    //
    //                                        FirebaseMessaging.getInstance().subscribeToTopic(projectTopic)
    //                                            .addOnCompleteListener { task ->
    //                                                if (task.isSuccessful) {
    //                                                    Log.d("FCM", "Subscribed to topic successfully")
    //                                                } else {
    //                                                    Log.d("FCM", "Failed to subscribe to topic",)
    //                                                }
    //                                            }""")
    lateinit var workManager: WorkManager
    fun initialize(context: Context) {
        workManager = WorkManager.getInstance(context)
    }
    fun sendFCMNotification(fcmToken: String, notification:NotificationEntity) {
        val payloadJsonObject =buildNotificationPayload(fcmToken, notification)
        payloadJsonObject?.let {
            val payloadInputData = Data.Builder()
                .putString(NotificationsFCMWorker.PAYLOAD_DATA, payloadJsonObject.toString())
                .build()

            val contraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val workRequest = OneTimeWorkRequestBuilder<NotificationsFCMWorker>()
                .setConstraints(contraints)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 500L, TimeUnit.MICROSECONDS)
                .setInputData(payloadInputData)
                .build()

            workManager.enqueue(workRequest)
        }
    }

    fun sendFCMNotificationToTopic(topic: String, notification: NotificationEntity) {
        val payloadJsonObject = buildNotificationPayloadForTopic(topic, notification)
        payloadJsonObject?.let {
            val payloadInputData = Data.Builder()
                .putString(NotificationsFCMWorker.PAYLOAD_DATA, payloadJsonObject.toString())
                .build()

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val workRequest = OneTimeWorkRequestBuilder<NotificationsFCMWorker>()
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 500L, TimeUnit.MICROSECONDS)
                .setInputData(payloadInputData)
                .build()

            workManager.enqueue(workRequest)
        }
    }

    private fun buildNotificationPayloadForTopic(topic: String, notification: NotificationEntity): JsonObject? {

        val payload = JsonObject()
        val data = JsonObject()

        payload.addProperty(N.TO, "/topics/$topic")
        data.addProperty(N.TITLE, notification.title)
        data.addProperty(N.BODY, notification.message)
        data.addProperty(N.TYPE, notification.notificationType)
        data.addProperty(N.ADDRESS,notification.address)
        data.addProperty(N.fromUser,notification.fromUser)
        data.addProperty(N.channelId,notification.channelID)

        payload.add(N.DATA, data)

        return payload
    }

    private fun buildNotificationPayload(fcmToken: String, notification: NotificationEntity): JsonObject? {
        if(notification.notificationType == NotificationType.CHAT_MESSAGE_NOTIFICATION.name){
            val payload = JsonObject()
            val data = JsonObject()
            payload.addProperty(N.TO, fcmToken)
            data.addProperty(N.TITLE, notification.title)
            data.addProperty(N.BODY, notification.message)
            data.addProperty(N.TYPE, notification.notificationType)
            payload.add(N.DATA, data)
            return payload
        }
        if(notification.notificationType == NotificationType.NEW_POST_NOTIFICATION.name){
            val payload = JsonObject()
            val data = JsonObject()
            payload.addProperty(N.TO, fcmToken)
            data.addProperty(N.TITLE, notification.title)
            data.addProperty(N.BODY, notification.message)
            data.addProperty(N.TYPE, notification.notificationType)
            data.addProperty(N.ADDRESS,notification.address)
            payload.add(N.DATA,data)
            return payload
        }
        return null
    }
}