package com.sandeveloper.jsscolab.domain.Modules.Messages

data class userProfileResponse(
    val success: Boolean,
    val message: String,
    val room: Room
)