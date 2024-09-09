package com.sandeveloper.jsscolab.domain.Modules.Auth

data class OtpVerifiedResponse (
    val success:Boolean,
    val message:String,
    val temp_token:String?
)