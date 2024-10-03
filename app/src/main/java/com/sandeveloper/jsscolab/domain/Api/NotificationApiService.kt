package com.sandeveloper.jsscolab.domain.Api

import com.sandeveloper.jsscolab.domain.Modules.Notification.ChatNotificationRequest
import com.sandeveloper.jsscolab.domain.Modules.Notification.NotificationResponse
import com.sandeveloper.jsscolab.domain.Modules.Notification.PostNotificationRequest
import com.sandeveloper.jsscolab.domain.Modules.Notification.SubscribeRequest
//import com.sandeveloper.jsscolab.BuildConfig
import retrofit2.Response

import retrofit2.http.Body

import retrofit2.http.POST


interface NotificationApiService {

    @POST("/subscribe")
    suspend fun subscribeToTopic(
        @Body subscribeRequest: SubscribeRequest
    ): Response<NotificationResponse>

    @POST("/send-chat-notification")
    suspend fun sendChatNotification(
        @Body chatRequest: ChatNotificationRequest
    ): Response<NotificationResponse>

    @POST("/send-post-notification")
    suspend fun sendPostNotification(
        @Body postRequest: PostNotificationRequest
    ): Response<NotificationResponse>
}
