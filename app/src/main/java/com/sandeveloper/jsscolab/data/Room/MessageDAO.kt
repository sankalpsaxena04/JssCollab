package com.sandeveloper.jsscolab.data.Room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sandeveloper.jsscolab.domain.Modules.Messages.MessageEntity

@Dao

interface MessageDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Query("SELECT * FROM messages WHERE roomId = :roomId ORDER BY time ASC")
    fun getMessagesForRoom(roomId: String): LiveData<List<MessageEntity>>

    @Query("DELETE FROM messages WHERE roomId = :roomId")
    suspend fun deleteMessagesForRoom(roomId: String)
}
