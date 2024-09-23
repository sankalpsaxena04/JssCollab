package com.sandeveloper.jsscolab.domain.Modules.Profile

import com.sandeveloper.jsscolab.domain.Modules.Post.Photo

data class details(
    val phone:String?,
    val email:String?,
    val full_name:String?,
    val admission_number:String?,
    val address:String?,
    val photo:Photo?
)
