package com.sandeveloper.jsscolab.domain.Modules.Auth

data class ForgetPasswordRequest (
    val phone:Long,
    val password:String,
    val temp_token:String
)