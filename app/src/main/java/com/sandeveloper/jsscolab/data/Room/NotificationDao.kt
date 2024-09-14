package com.sandeveloper.jsscolab.data.Room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sandeveloper.jsscolab.domain.Modules.NotificationEntity

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: NotificationEntity)

    @Update
    suspend fun update(notification: NotificationEntity)

    @Delete
    suspend fun delete(notification: NotificationEntity)

    @Query("SELECT * FROM notifications WHERE notificationID = :notificationID")
    suspend fun getNotificationById(notificationID: String): NotificationEntity?

    @Query("SELECT * FROM notifications")
    suspend fun getAllNotifications(): List<NotificationEntity>


    @Query("SELECT COUNT(*) FROM notifications WHERE timeStamp > :projectTimeStamp ")
    suspend fun getUnreadNotificationCount( projectTimeStamp : Long): Int

}
