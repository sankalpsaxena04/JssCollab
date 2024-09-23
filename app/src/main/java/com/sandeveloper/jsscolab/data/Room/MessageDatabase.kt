package com.sandeveloper.jsscolab.data.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sandeveloper.jsscolab.domain.Modules.Messages.MessageEntity

@Database(entities = [MessageEntity::class], version = 2, exportSchema = false)
abstract class MessageDatabase: RoomDatabase() {

    abstract fun messageDAO(): MessageDAO
}