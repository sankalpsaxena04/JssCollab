package com.sandeveloper.jsscolab.domain.Modules.Post

data class postUnit(
    val _id: String,
    val sender: Sender?,
    val app: App,
    val category: String,
    val comment: String,
    val sender_contribution: Number,
    val filter: Filter,
    val total_required_amount: Number,
    val expiration_date: Long
)