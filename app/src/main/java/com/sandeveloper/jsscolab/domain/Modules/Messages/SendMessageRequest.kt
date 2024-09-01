package com.sandeveloper.jsscolab.domain.Modules.Messages

data class SendMessageRequest (
    val room_id: String,
    val text: String,
    val timeSent:Long

)
