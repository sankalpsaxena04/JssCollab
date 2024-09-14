package com.sandeveloper.jsscolab.data.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sandeveloper.jsscolab.domain.Modules.Messages.MessageEntity

@Dao
interface MessageDAO {

    @Insert
    suspend fun addMessage(message: MessageEntity)

    @Query("SELECT * from messages WHERE roomId==:roomId")
    suspend fun getMessages(roomId: String): List<MessageEntity>

    @Query("SELECT * FROM messages WHERE sender LIKE '%'||:it||'%'  OR text LIKE '%'||:it||'%'")
    suspend fun search(it:String):List<MessageEntity>
}