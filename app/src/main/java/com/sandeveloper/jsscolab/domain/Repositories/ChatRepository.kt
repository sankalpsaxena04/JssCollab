package com.sandeveloper.jsscolab.domain.Repositories

import com.google.gson.Gson
import com.sandeveloper.jsscolab.data.Room.Message
import com.sandeveloper.jsscolab.data.Room.MessageDAO
import com.sandeveloper.jsscolab.data.Room.People
import com.sandeveloper.jsscolab.data.Room.PeopleDao
import com.sandeveloper.jsscolab.di.IoDispatcher
import com.sandeveloper.jsscolab.domain.Api.MessageApi
import com.sandeveloper.jsscolab.domain.Interfaces.MessageRepository
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Messages.CheckForMessagesResponse
import com.sandeveloper.jsscolab.domain.Modules.Messages.GetMessagesResponse
import com.sandeveloper.jsscolab.domain.Modules.Messages.InboxData
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
    private val peopleDao: PeopleDao,
    private val messageDao: MessageDAO,
) : MessageRepository {


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
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, commonResponse::class.java)
                serverResult(ServerResult.Failure(Exception(errorResponse.message)))
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

    override suspend fun checkForMessages(serverResult: (ServerResult<CheckForMessagesResponse>) -> Unit) {
        handleApiResponse(
            apiCall = { messageApi.checkForMessages() },
            serverResult = serverResult
        )
    }

    override suspend fun getUserProfile(roomId: String?, userId: String?, serverResult: (ServerResult<userProfileResponse>) -> Unit) {
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
      //  messageDAO.deleteMessagesForRoom(roomId) // Delete from local database
    }

    override suspend fun insertPersonDB(person: People): ServerResult<Unit> {
        return try {
            peopleDao.insertPerson(person)
            ServerResult.Success(Unit)
        } catch (e: Exception) {
            ServerResult.Failure(e)
        }
    }

    override suspend fun getPersonByIdDB(senderId: String): ServerResult<People?> {
        return try {
            val person = peopleDao.getPersonById(senderId)
            ServerResult.Success(person)
        } catch (e: Exception) {
            ServerResult.Failure(e)
        }
    }

    override suspend fun updateBlockStatusDB(senderId: String, isBlocked: Boolean): ServerResult<Unit> {
        return try {
            peopleDao.updateBlockStatus(senderId, isBlocked)
            ServerResult.Success(Unit)
        } catch (e: Exception) {
            ServerResult.Failure(e)
        }
    }

    override suspend fun deletePersonDB(senderId: String): ServerResult<Unit> {
        return try {
            peopleDao.deletePerson(senderId)
            ServerResult.Success(Unit)
        } catch (e: Exception) {
            ServerResult.Failure(e)
        }
    }

    override suspend fun getInboxDataDB(): ServerResult<List<InboxData>> {
        return try {
            val inboxData = peopleDao.getInboxData()
            ServerResult.Success(inboxData)
        } catch (e: Exception) {
            ServerResult.Failure(e)
        }
    }

    override suspend fun roomExistsDB(roomId: String): Boolean {
        return try {
            val exists = peopleDao.roomExists(roomId)
            exists
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun insertMessageDB(message: Message): ServerResult<Unit> {
        return try {
            messageDao.insertMessage(message)
            ServerResult.Success(Unit)
        } catch (e: Exception) {
            ServerResult.Failure(e)
        }
    }

    override suspend fun getMessagesByRoomIdDB(roomId: String): ServerResult<List<Message>> {
        return try {
            val messages = messageDao.getMessagesByRoomId(roomId)
            ServerResult.Success(messages)
        } catch (e: Exception) {
            ServerResult.Failure(e)
        }
    }

    override suspend fun updateMessageReadStatusDB(messageId: Int, isRead: Boolean): ServerResult<Unit> {
        return try {
            messageDao.updateMessageReadStatus(messageId, isRead)
            ServerResult.Success(Unit)
        } catch (e: Exception) {
            ServerResult.Failure(e)
        }
    }

    override suspend fun deleteMessagesByRoomIdDB(roomId: String): ServerResult<Unit> {
        return try {
            messageDao.deleteMessagesByRoomId(roomId)
            ServerResult.Success(Unit)
        } catch (e: Exception) {
            ServerResult.Failure(e)
        }
    }


}
