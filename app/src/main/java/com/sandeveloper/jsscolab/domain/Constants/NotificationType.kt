package com.sandeveloper.jsscolab.domain.Constants


enum class NotificationType(val title: String, val priority : Int) {
    REQUEST_FAILED_NOTIFICATION("Request failed", 3),
    CHAT_MESSAGE_NOTIFICATION("New Message", 2),
    NEW_POST_NOTIFICATION("New Post", 1),

}