package com.sandeveloper.jsscolab.domain.Modules.Messages

data class CheckForMessagesResponse (
    val success: Boolean,
    val message: String,
    val messagesCount:Int?

)
