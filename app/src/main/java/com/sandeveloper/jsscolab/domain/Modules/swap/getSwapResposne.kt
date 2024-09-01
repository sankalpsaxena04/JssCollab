package com.sandeveloper.jsscolab.domain.Modules.swap

data class getSwapResposne(
    val success: Boolean,              // Indicates whether the operation was successful
    val message: String,              // Message describing the result
    val swaps: List<Swap>?
)
