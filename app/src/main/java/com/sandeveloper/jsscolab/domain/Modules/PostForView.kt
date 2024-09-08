package com.sandeveloper.jsscolab.domain.Modules

import com.sandeveloper.jsscolab.domain.Modules.Post.postUnit

data class PostForView(
    val comment: String,
    val senderContribution: String,
    val totalRequiredAmount: String,
    val fullName: String,
    val photo: String,
    val rating: Number,
    val appName: String
)
fun postUnit.toPostSummary(): PostForView {
    return PostForView(
        comment = this.comment,
        senderContribution = this.sender_contribution,
        totalRequiredAmount = this.total_required_amount,
        fullName = this.sender.full_name,
        photo = this.sender.photo,
        rating = this.sender.rating,
        appName = this.app.name
    )
}
