package com.sandeveloper.jsscolab.domain.Modules.app

data class getCategoryResponse (
    val success: Boolean,
    val message: String,
    val categories: List<String>

)
