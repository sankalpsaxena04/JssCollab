package com.sandeveloper.jsscolab.domain.Modules.Post

data class createPost(
    val app: List<String>,
    val category: String,
    val comment: String?,
    val sender_contribution: Number,
    val filter: Filter,
    val total_required_amount: Number,
    val expiration_date: Long
)