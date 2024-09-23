package com.sandeveloper.jsscolab.domain.Modules

import com.sandeveloper.jsscolab.domain.Modules.Post.Posts

data class PostForView(
    val comment: String,
    val senderContribution: Number,
    val totalRequiredAmount: Number,
    val fullName: String,
    val userphoto: String?,
    val rating: Number,
    val category: String,
    val expiresIn: String
//    val appphoto: String?
)
// TODO("getappphoto from get-apps")
fun Posts.toPostSummary(): PostForView {
    return PostForView(
        comment = this.comment?:"",
        senderContribution = this.sender_contribution,
        totalRequiredAmount = this.total_required_amount,
        fullName = this.sender!!.full_name,
        userphoto = this.sender.photo?.secure_url,
        rating = this.sender.rating,
        category = this.category,
        expiresIn = this.expiration_date
//        appphoto = this.apps?
    )
}
