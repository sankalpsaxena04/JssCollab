package com.sandeveloper.jsscolab.domain.Modules.Auth

data class OtpRequest(
    val phone:Long,
    val action:String
)
