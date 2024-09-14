package com.sandeveloper.jsscolab.data.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sandeveloper.jsscolab.domain.Modules.app.AppEntity

@Database(entities = [AppEntity::class], version = 1, exportSchema = false)
abstract class AppsDatabase:RoomDatabase() {
    abstract fun appsDao(): AppsDao
}