package com.sandeveloper.jsscolab.domain.Repositories

import com.google.gson.Gson
import com.sandeveloper.jsscolab.di.IoDispatcher
import com.sandeveloper.jsscolab.domain.Api.MessageApi
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Messages.CheckForMessagesResponse
import com.sandeveloper.jsscolab.domain.Modules.Messages.GetMessagesResponse
import com.sandeveloper.jsscolab.domain.Modules.Messages.Message
import com.sandeveloper.jsscolab.domain.Modules.Messages.RoomIdResponse
import com.sandeveloper.jsscolab.domain.Modules.Messages.SendMessageRequest
import com.sandeveloper.jsscolab.domain.Modules.commonResponse
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val messageApi: MessageApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    private lateinit var socket: Socket

    fun initializeSocket() {
        socket = IO.socket("https://your-socket-url.com")
    }

    fun connectSocket() {
        socket.connect()
    }

    fun disconnectSocket() {
        socket.disconnect()
    }

    // Generalized API response handler
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

    // API Calls using handleApiResponse

    suspend fun createRoom(
        userId: String,
        serverResult: (ServerResult<RoomIdResponse>) -> Unit
    ) {
        handleApiResponse(
            apiCall = { messageApi.createRoom(mapOf("user_id" to userId)) },
            serverResult = serverResult
        )
    }

    suspend fun getRoomId(
        userId: String,
        serverResult: (ServerResult<RoomIdResponse>) -> Unit
    ) {
        handleApiResponse(
            apiCall = { messageApi.getRoomId(userId) },
            serverResult = serverResult
        )
    }

    suspend fun getMessages(
        roomId: String,
        serverResult: (ServerResult<GetMessagesResponse>) -> Unit
    ) {
        handleApiResponse(
            apiCall = { messageApi.getMessages(roomId) },
            serverResult = serverResult
        )
    }

    suspend fun sendMessage(
        request: SendMessageRequest,
        serverResult: (ServerResult<commonResponse>) -> Unit
    ) {
        handleApiResponse(
            apiCall = { messageApi.sendMessage(request) },
            serverResult = serverResult
        )
    }

    suspend fun checkForMessages(
        roomId: String,
        serverResult: (ServerResult<CheckForMessagesResponse>) -> Unit
    ) {
        handleApiResponse(
            apiCall = { messageApi.checkForMessages(roomId) },
            serverResult = serverResult
        )
    }

    suspend fun deleteMessages(
        roomId: String,
        serverResult: (ServerResult<commonResponse>) -> Unit
    ) {
        handleApiResponse(
            apiCall = { messageApi.deleteMessages(roomId) },
            serverResult = serverResult
        )
    }

    // Socket IO methods remain unchanged
    fun listenForMessages(onMessageReceived: (ServerResult<Message>) -> Unit) {
        socket.on("message") { args ->
            try {
                val message = Gson().fromJson(args[0].toString(), Message::class.java)
                onMessageReceived(ServerResult.Success(message))
            } catch (e: Exception) {
                onMessageReceived(ServerResult.Failure(e))
            }
        }
    }

    fun sendMessageSocket(message: Message) {
        socket.emit("send_message", Gson().toJson(message))
    }
}
