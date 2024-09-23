package com.sandeveloper.jsscolab.domain.Interfaces

import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Messages.CheckForMessagesResponse
import com.sandeveloper.jsscolab.domain.Modules.Messages.GetMessagesResponse
import com.sandeveloper.jsscolab.domain.Modules.Messages.Message
import com.sandeveloper.jsscolab.domain.Modules.Messages.MessageEntity
import com.sandeveloper.jsscolab.domain.Modules.Messages.RoomIdResponse
import com.sandeveloper.jsscolab.domain.Modules.Messages.SendMessageRequest
import com.sandeveloper.jsscolab.domain.Modules.Messages.userProfileResponse
import com.sandeveloper.jsscolab.domain.Modules.commonResponse

interface MessageRepository {

    suspend fun getRoomId(userId: String, serverResult: (ServerResult<RoomIdResponse>) -> Unit)

    suspend fun getMessages(roomId: String, serverResult: (ServerResult<GetMessagesResponse>) -> Unit)

    suspend fun sendMessage(request: SendMessageRequest, serverResult: (ServerResult<commonResponse>) -> Unit)

    suspend fun checkForMessages(roomId: String, serverResult: (ServerResult<CheckForMessagesResponse>) -> Unit)

    suspend fun getUserProfile(roomId: String, userId: String, serverResult: (ServerResult<userProfileResponse>) -> Unit)

    suspend fun deleteMessages(roomId: String, serverResult: (ServerResult<commonResponse>) -> Unit)

    fun initializeSocket()

    fun connectSocket()

    fun disconnectSocket()

    fun listenForMessages(onMessageReceived: (ServerResult<Message>) -> Unit)

    fun sendMessageSocket(message: MessageEntity)
}
