package com.sandeveloper.jsscolab.domain.Modules.Post

data class Filter(
    val my_year: Boolean,
    val address: List<String>,
    val branch: List<String>
)