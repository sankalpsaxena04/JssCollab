package com.sandeveloper.jsscolab.domain.Modules.Post

data class updatePostRequest(
    val post_id: String,
    val app: String,
    val category: String,
    val comment: String,
    val sender_contribution: Int,
    val filter: Filter,
    val total_required_amount: Int,
    val expiration_date: String
)