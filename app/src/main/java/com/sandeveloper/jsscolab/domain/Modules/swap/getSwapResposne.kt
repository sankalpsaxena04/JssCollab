package com.sandeveloper.jsscolab.domain.Modules.swap

data class getSwapResposne(
    val success: Boolean,
    val message: String,
    val swaps: List<Swap>?
)
