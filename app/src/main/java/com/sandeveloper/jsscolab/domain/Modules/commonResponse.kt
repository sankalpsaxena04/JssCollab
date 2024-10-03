package com.sandeveloper.jsscolab.domain.Modules

data class commonResponse(
    val success:Boolean,
    val message:String,
    val FCM_token:String?
)
