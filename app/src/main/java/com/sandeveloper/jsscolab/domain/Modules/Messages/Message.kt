package com.sandeveloper.jsscolab.domain.Modules.Messages

import java.util.Date

data class Message(
//    val user1:String,
//    val user2:String,
    val sender:String,
    val text:String,
    val timeSent: Date
)
