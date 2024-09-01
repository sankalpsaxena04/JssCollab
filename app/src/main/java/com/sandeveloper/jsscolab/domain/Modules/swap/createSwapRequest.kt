package com.sandeveloper.jsscolab.domain.Modules.swap

import com.sandeveloper.jsscolab.domain.Modules.Post.Filter

data class createSwapRequest(
    val to_give: Number,
    val to_take: Number,
    val filter: Filter,
    val expiration_date: Long
)