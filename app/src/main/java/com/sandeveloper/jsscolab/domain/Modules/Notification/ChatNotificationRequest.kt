package com.sandeveloper.jsscolab.domain.Modules.Notification

data class ChatNotificationRequest(
    val recipientToken: String,
    val title: String,
    val body: String
)