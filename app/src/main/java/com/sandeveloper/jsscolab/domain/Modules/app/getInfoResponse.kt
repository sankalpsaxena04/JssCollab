package com.sandeveloper.jsscolab.domain.Modules.app

data class getInfoResponse(
    val success: Boolean,
    val message: String,
    val apps: List<getInfo>?
)
