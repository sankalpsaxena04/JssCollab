package com.sandeveloper.jsscolab.domain.Repositories

import com.google.gson.Gson
import com.sandeveloper.jsscolab.data.Room.MessageDAO
import com.sandeveloper.jsscolab.di.IoDispatcher
import com.sandeveloper.jsscolab.domain.Api.MessageApi
import com.sandeveloper.jsscolab.domain.Interfaces.MessageRepository
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Messages.CheckForMessagesResponse
import com.sandeveloper.jsscolab.domain.Modules.Messages.GetMessagesResponse
import com.sandeveloper.jsscolab.domain.Modules.Messages.Message
import com.sandeveloper.jsscolab.domain.Modules.Messages.MessageEntity
import com.sandeveloper.jsscolab.domain.Modules.Messages.RoomIdResponse
import com.sandeveloper.jsscolab.domain.Modules.Messages.SendMessageRequest
import com.sandeveloper.jsscolab.domain.Modules.Messages.userProfileResponse
import com.sandeveloper.jsscolab.domain.Modules.commonResponse
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


class ChatRepository @Inject constructor(
    private val messageApi: MessageApi,
    private val messageDAO: MessageDAO, // Inject the DAO
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : MessageRepository {

    private lateinit var socket: Socket

    override fun initializeSocket() {
        socket = IO.socket("https://your-socket-url.com")
    }

    override fun connectSocket() {
        socket.connect()
    }

    override fun disconnectSocket() {
        socket.disconnect()
    }
    private suspend fun <T> handleApiResponse(
        apiCall: suspend () -> Response<T>,
        serverResult: (ServerResult<T>) -> Unit,
        onSuccess: (T) -> Unit = {}
    ) {
        try {
            serverResult(ServerResult.Progress)
            val response = apiCall()
            if (response.isSuccessful) {
                response.body()?.let {
                    onSuccess(it)
                    serverResult(ServerResult.Success(it))
                } ?: serverResult(ServerResult.Failure(Exception("Response body is null")))
            } else {
                serverResult(ServerResult.Failure(Exception("Failed with status code: ${response.code()}")))
            }
        } catch (e: Exception) {
            serverResult(ServerResult.Failure(e))
        }
    }

    override suspend fun getRoomId(userId: String, serverResult: (ServerResult<RoomIdResponse>) -> Unit) {
        handleApiResponse(
            apiCall = { messageApi.getRoomId(mapOf("user_id" to userId)) },
            serverResult = serverResult
        )
    }

    override suspend fun getMessages(roomId: String, serverResult: (ServerResult<GetMessagesResponse>) -> Unit) {
        handleApiResponse(
            apiCall = { messageApi.getMessages(roomId) },
            serverResult = serverResult
        )
    }

    override suspend fun sendMessage(request: SendMessageRequest, serverResult: (ServerResult<commonResponse>) -> Unit) {
        handleApiResponse(
            apiCall = { messageApi.sendMessage(request) },
            serverResult = serverResult
        )
    }

    override suspend fun checkForMessages(roomId: String, serverResult: (ServerResult<CheckForMessagesResponse>) -> Unit) {
        handleApiResponse(
            apiCall = { messageApi.checkForMessages(roomId) },
            serverResult = serverResult
        )
    }

    override suspend fun getUserProfile(roomId: String, userId: String, serverResult: (ServerResult<userProfileResponse>) -> Unit) {
        handleApiResponse(
            apiCall = { messageApi.getUserProfile(roomId, userId) },
            serverResult = { result ->
                if (result is ServerResult.Success) {
                    val userProfile = result.data.room.user
                    // Store user profile info in MessageEntity for future use
                    // Note: You would likely want to store this in a different table or cache
                }
                serverResult(result)
            }
        )
    }

    override suspend fun deleteMessages(roomId: String, serverResult: (ServerResult<commonResponse>) -> Unit) {
        handleApiResponse(
            apiCall = { messageApi.deleteMessages(roomId) },
            serverResult = serverResult
        )
        messageDAO.deleteMessagesForRoom(roomId) // Delete from local database
    }

    // Socket IO methods
    override fun listenForMessages(onMessageReceived: (ServerResult<Message>) -> Unit) {
        socket.on("message") { args ->
            try {
                val message = Gson().fromJson(args[0].toString(), Message::class.java)
                onMessageReceived(ServerResult.Success(message))

                // Insert received message into database
                CoroutineScope(ioDispatcher).launch {
                    //TODO("refactor this")
                    messageDAO.insertMessage(MessageEntity(roomId = "message.", senderId = message.sender, text = message.text, time = message.timeSent, isSender = false))
                }

            } catch (e: Exception) {
                onMessageReceived(ServerResult.Failure(e))
            }
        }
    }

    override fun sendMessageSocket(message: MessageEntity) {
        socket.emit("send_message", Gson().toJson(Message(message.senderId, message.text, message.time)))

        // Store the message locally once sent
        CoroutineScope(ioDispatcher).launch {

            messageDAO.insertMessage(message)
        }
    }
}
