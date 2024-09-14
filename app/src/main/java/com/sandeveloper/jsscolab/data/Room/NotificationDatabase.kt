package com.sandeveloper.jsscolab.data.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sandeveloper.jsscolab.domain.Modules.NotificationEntity

@Database(entities = [NotificationEntity::class], version = 1)
abstract class NotificationDatabase : RoomDatabase() {

    // Step 2: Define an abstract function to get the NotificationDao
    abstract fun notificationDao(): NotificationDao

    // You can define other DAOs for additional entities here if needed.
}