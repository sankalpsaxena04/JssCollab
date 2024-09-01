package com.sandeveloper.jsscolab.domain.Modules.Auth

data class VerifiOtp(
    val phone:Long,
    val otp:Int,
    val orderId:String
)
