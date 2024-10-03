package com.sandeveloper.jsscolab.domain.HelperClasses

import com.sandeveloper.jsscolab.data.Room.SwapDAO
import com.sandeveloper.jsscolab.domain.Constants.Endpoints
import com.sandeveloper.jsscolab.domain.Modules.Post.Filter
import com.sandeveloper.jsscolab.domain.Modules.Post.Posts
import com.sandeveloper.jsscolab.domain.Modules.swap.Swap
import com.sandeveloper.jsscolab.presentation.Main.home.HomeViewModel
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


fun String.toOriginalCategories(): String {
        return when (this) {
            Endpoints.categories.Pharmaceuticals -> "pharmaceutical"
            Endpoints.categories.QuickCommerce -> "quick-commerce"
            Endpoints.categories.FoodDelivery -> "food-delivery"
            Endpoints.categories.ECommerce -> "e-commerce"
            Endpoints.categories.SharedCab -> "cab"
            Endpoints.categories.Exchange -> "subscription-service"
            else -> "other"
        }

    }

fun Swap.toPost():Posts{
    return Posts(
        this._id,
        this.sender!!,
        emptyList(),
        category = Endpoints.categories.Exchange,
        comment = "To Give: ${this.to_give.map {it.name }.joinToString(", ")} \nTo Receive: ${this.to_take.map { it.name}.joinToString(", ")}",
        sender_contribution=0,
        filter= this.filter,
        total_required_amount=0,
        expiration_date= "0"

    )

}
fun getExpirationTime(isoString: String): String {
    val dateTime = LocalDateTime.parse(isoString, DateTimeFormatter.ISO_DATE_TIME)
    val now = LocalDateTime.now(ZoneOffset.UTC)
    val duration = Duration.between(now, dateTime)

    return when {
        duration.toDays() > 0 -> "Expires in ${duration.toDays()} day(s)"
        duration.toHours() > 0 -> "Expires in ${duration.toHours()} hour(s)"
        duration.toMinutes() > 0 -> "Expires in ${duration.toMinutes()} minute(s)"
        else -> "Expired"
    }
}