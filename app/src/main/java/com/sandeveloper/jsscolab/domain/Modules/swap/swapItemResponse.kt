package com.sandeveloper.jsscolab.domain.Modules.swap

data class swapItemResponse(
    val success: Boolean,
    val message: String,
    val swapItems: List<SwapEntity>
)



