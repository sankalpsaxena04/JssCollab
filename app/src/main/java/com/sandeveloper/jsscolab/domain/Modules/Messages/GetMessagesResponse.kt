package com.sandeveloper.jsscolab.domain.Modules.Messages

data class GetMessagesResponse(
    val success: Boolean,
    val message: String,
    val messages: List<Message>?

)
