package com.sandeveloper.jsscolab.domain.Modules.swap

import com.sandeveloper.jsscolab.domain.Modules.Post.Filter

data class getSwapsRequest(
    val search:String?,
    val items_giving: List<String>?,
    val items_wanted: List<String>?,
    val address: String?,
    val my_year: String?
)
