package com.sandeveloper.jsscolab.domain.Modules

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sandeveloper.jsscolab.domain.Constants.Endpoints

@Entity(tableName = Endpoints.ROOM.NOTIFICATIONS.NOTIFICATIONS_TABLE)
data class NotificationEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "notificationID")
    val notificationID: String,

    @ColumnInfo(name = "notificationType")
    val notificationType: String,


    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "message")
    val message: String,

    @ColumnInfo(name = "fromUser")
    val fromUser: String,

    @ColumnInfo(name = "toUser")
    val toUser: String,

    @ColumnInfo(name = "timeStamp")
    val timeStamp: Long,

    @ColumnInfo(name = "address")
    val address:String
    ,

    val lastUpdated:Long?=null,

    val channelID:String="",

)
