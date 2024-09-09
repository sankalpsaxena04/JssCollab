package com.sandeveloper.jsscolab.domain.Modules.Auth

data class AuthResponse(
    val success:Boolean,
    val message:String,
    val token:String?
)
