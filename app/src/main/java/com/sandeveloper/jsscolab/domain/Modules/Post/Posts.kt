package com.sandeveloper.jsscolab.domain.Modules.Post

data class Posts(
    val _id: String,
    val sender: Sender,
    val apps: List<String>?,
    val category: String,
    val comment: String?,
    val sender_contribution: Int,
    val filter: Filter?,
    val total_required_amount: Int,
    val expiration_date: String
)