package com.sandeveloper.jsscolab.domain.Modules.Notification

data class SubscribeRequest(
    val fcmToken: String,
    val topic: String
)