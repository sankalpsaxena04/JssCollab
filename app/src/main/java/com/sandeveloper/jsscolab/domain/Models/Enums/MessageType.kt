package com.sandeveloper.jsscolab.domain.Models.Enums


enum class MessageType {
    NORMAL_MSG,   // containing only textual content
    IMAGE_MSG,    // containing image only
    REPLY_MSG,    // containing reply to another msg
    LINK_MSG,
    FILE_MSG,    // containing only file
    ACTIVITY_UPDATE; //Update messages

    companion object {
        fun fromString(value: String): MessageType? {
            return values().find { it.name == value }
        }
    }
}
