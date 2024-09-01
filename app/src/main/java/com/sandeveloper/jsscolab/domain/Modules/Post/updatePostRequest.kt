package com.sandeveloper.jsscolab.domain.Modules.Post

data class updatePostRequest(
    val post_id: String,
    val app: String?,
    val category: String?,
    val comment: String?,
    val sender_contribution: Number?,
    val filter: Filter?,
    val total_required_amount: Number?,
    val expiration_date: Long?
)