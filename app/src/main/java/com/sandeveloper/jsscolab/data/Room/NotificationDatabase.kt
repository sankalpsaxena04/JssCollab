package com.sandeveloper.jsscolab.data.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sandeveloper.jsscolab.domain.Modules.NotificationEntity

@Database(entities = [NotificationEntity::class], version = 1)
abstract class NotificationDatabase : RoomDatabase() {

    abstract fun notificationDao(): NotificationDao


}