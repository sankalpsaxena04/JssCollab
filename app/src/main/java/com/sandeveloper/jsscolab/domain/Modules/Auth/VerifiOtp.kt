package com.sandeveloper.jsscolab.domain.Modules.Auth

data class VerifiOtp(
    val phone:Int,
    val otp:Int,
    val orderId:String
)
