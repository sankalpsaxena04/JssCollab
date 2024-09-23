package com.sandeveloper.jsscolab.domain.Modules.Profile

data class CreateProfile(
    val fullName: String,
    val admissionNumber: String,
    val address: String,
    val FCM_token:String
)
