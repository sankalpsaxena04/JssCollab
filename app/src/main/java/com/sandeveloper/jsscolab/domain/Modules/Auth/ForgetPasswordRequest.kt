package com.sandeveloper.jsscolab.domain.Modules.Auth

data class ForgetPasswordRequest (
    val phone:Int,
    val password:String,
    val temp_token:String
)