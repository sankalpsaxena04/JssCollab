package com.sandeveloper.jsscolab.domain.Modules.swap

import com.sandeveloper.jsscolab.domain.Modules.Post.Filter
import com.sandeveloper.jsscolab.domain.Modules.Post.Sender
import java.util.Date

data class Swap(
    val _id: String,
    val sender: Sender?,
    val to_give: List<SwapItem>,
    val to_take: List<SwapItem>,
    val filter: Filter
)
