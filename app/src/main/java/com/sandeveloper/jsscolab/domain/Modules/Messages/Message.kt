package com.sandeveloper.jsscolab.domain.Modules.Messages

import com.sandeveloper.jsscolab.domain.Modules.Post.Sender
import java.util.Date

data class Message(
//    val user1:String,
//    val user2:String,
    val sender:String,
    val text:String,
    val timeSent: Long
)
