package com.sandeveloper.jsscolab.domain.Modules.Auth

data class OtpRequest(
    val phone:Int,
    val action:String
)
