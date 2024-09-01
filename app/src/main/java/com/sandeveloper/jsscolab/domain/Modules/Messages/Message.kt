package com.sandeveloper.jsscolab.domain.Modules.Messages

import java.util.Date

data class Message(
    val sender:String,
    val text:String,
    val timeSent: Date
)
