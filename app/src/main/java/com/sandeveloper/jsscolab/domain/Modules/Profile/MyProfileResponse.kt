package com.sandeveloper.jsscolab.domain.Modules.Profile

data class MyProfileResponse(
    val phone:Long,
    val email:String,
    val full_name:String,
    val admission_number:String,
    val address:String,
    val photo:String
)
