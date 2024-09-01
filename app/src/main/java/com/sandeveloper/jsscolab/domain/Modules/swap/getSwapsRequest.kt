package com.sandeveloper.jsscolab.domain.Modules.swap

import com.sandeveloper.jsscolab.domain.Modules.Post.Filter

data class getSwapsRequest(
    val items_giving: List<String>?,                 // Amount or items to give
    val items_wanted: List<String>?,
    val my_year: String?,
    val address: String?
)
