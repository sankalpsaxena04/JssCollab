package com.sandeveloper.jsscolab.domain.Modules.Messages

data class User(
    val _id: String,
    val full_name: String,
    val photo: String?="",
    val rating: Double,
    val rated_count: Int
)