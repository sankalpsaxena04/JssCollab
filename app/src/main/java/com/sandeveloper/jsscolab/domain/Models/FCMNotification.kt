package com.sandeveloper.jsscolab.domain.Models

import com.sandeveloper.jsscolab.domain.Constants.NotificationType

data class FCMNotification constructor(
    val notificationType: NotificationType,
    val title:String = "",
    val desc: String = "",
    val message: String = "",
    val address:String? = null
)
