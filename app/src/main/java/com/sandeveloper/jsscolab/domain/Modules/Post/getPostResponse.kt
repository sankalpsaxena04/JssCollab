package com.sandeveloper.jsscolab.domain.Modules.Post

data class getPostResponse(
    val success: Boolean,
    val message: String,
    val posts: List<Posts>?
)
