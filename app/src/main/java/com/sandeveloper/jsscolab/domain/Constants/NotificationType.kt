package com.sandeveloper.jsscolab.domain.Constants


enum class NotificationType(val title: String, val priority : Int) {
    REQUEST_FAILED_NOTIFICATION("Request failed", 3),
    TASK_REQUEST_RECIEVED_NOTIFICATION("Work Request",3),

}