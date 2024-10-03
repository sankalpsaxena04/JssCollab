package com.sandeveloper.jsscolab.domain.Repositories

import com.sandeveloper.jsscolab.domain.Api.NotificationApiService
import com.sandeveloper.jsscolab.domain.Modules.Notification.ChatNotificationRequest
import com.sandeveloper.jsscolab.domain.Modules.Notification.NotificationResponse
import com.sandeveloper.jsscolab.domain.Modules.Notification.PostNotificationRequest
import com.sandeveloper.jsscolab.domain.Modules.Notification.SubscribeRequest
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(
    private val notificationApi: NotificationApiService
) {
    suspend fun subscribeToTopic(token: String, topic: String): NotificationResponse {
        val request = SubscribeRequest(fcmToken = token, topic = topic)
        return notificationApi.subscribeToTopic(request).body() ?: NotificationResponse(false, "Error")
    }

    suspend fun sendChatNotification(token: String, title: String, body: String): NotificationResponse {
        val request = ChatNotificationRequest(recipientToken = token, title = title, body = body)
        return notificationApi.sendChatNotification(request).body() ?: NotificationResponse(false, "Error")
    }

    suspend fun sendPostNotification(topic: String, title: String, body: String): NotificationResponse {
        val request = PostNotificationRequest(topic = topic, title = title, body = body)
        return notificationApi.sendPostNotification(request).body() ?: NotificationResponse(false, "Error")
    }
}
