package com.sandeveloper.jsscolab.domain.Modules.app

data class getAppResponse(
    val success: Boolean,
    val message: String,
    val apps: List<App>?
)
