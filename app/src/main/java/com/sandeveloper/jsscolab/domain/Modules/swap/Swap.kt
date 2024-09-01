package com.sandeveloper.jsscolab.domain.Modules.swap

import com.sandeveloper.jsscolab.domain.Modules.Post.Sender

data class Swap(
    val _id: String,
    val sender: Sender,
    val to_give: List<String>,
    val to_take: List<String>,
    val my_year: Boolean,
    val address: String
)
