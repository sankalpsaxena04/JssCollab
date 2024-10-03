package com.sandeveloper.jsscolab.data.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sandeveloper.jsscolab.domain.Modules.swap.SwapEntity

@Database(
    entities = [SwapEntity::class],version =1)
abstract class SwapDatabase:RoomDatabase() {
    abstract fun swapDao():SwapDAO

}