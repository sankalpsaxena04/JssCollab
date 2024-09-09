package com.sandeveloper.jsscolab.domain.Modules.Auth

data class OtpResponse(
    val success:Boolean,
    val message:String,
    val orderId:String?
)
