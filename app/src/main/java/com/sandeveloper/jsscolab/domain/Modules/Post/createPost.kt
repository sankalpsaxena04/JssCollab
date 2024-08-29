package com.sandeveloper.jsscolab.domain.Modules.Post

data class createPost(
    val app: String,
    val category: String,
    val comment: String,
    val sender_contribution: Int,
    val filter: String,
    val total_required_amount: Int,
    val expiration_date: String
)