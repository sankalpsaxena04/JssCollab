package com.sandeveloper.jsscolab.domain.Modules.swap

import com.sandeveloper.jsscolab.domain.Modules.Post.Filter

data class createSwapRequest(
    val to_give: List<String>,
    val to_take: List<String>,
    val filter: Filter,
    val expiration_date: Long
)