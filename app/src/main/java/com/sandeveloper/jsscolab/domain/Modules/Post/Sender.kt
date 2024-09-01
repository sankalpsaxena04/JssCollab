package com.sandeveloper.jsscolab.domain.Modules.Post

data class Sender(
    val _id: String,
    val full_name: String,
    val admission_number: String,
    val address: String,
    val photo: String,
    val rating: Number,
    val rated_count: Number
)