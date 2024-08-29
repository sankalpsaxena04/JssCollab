package com.sandeveloper.jsscolab.domain.Modules.Auth

data class signupRequest(
    val email:String,
    val phone:String,
    val password:String,
    val temp_token:String
)
