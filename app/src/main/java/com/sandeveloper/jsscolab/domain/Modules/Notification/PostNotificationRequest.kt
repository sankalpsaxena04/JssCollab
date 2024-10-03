package com.sandeveloper.jsscolab.domain.Modules.Notification

data class PostNotificationRequest(
    val topic: String,
    val title: String,
    val body: String
)