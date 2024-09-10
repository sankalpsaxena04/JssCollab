package com.sandeveloper.jsscolab.domain.Modules.Post

data class getPostsRequest(
    val search:String?,
    val apps: List<String>?,
    val categories: List<String>?,
    val addresses: List<String>?,
    val my_contribution: Number?,
    val address: String?,
    val my_year: Boolean?
)