package com.sandeveloper.jsscolab.domain.Api

import com.sandeveloper.jsscolab.domain.Modules.Messages.CheckForMessagesResponse
import com.sandeveloper.jsscolab.domain.Modules.Messages.GetMessagesResponse
import com.sandeveloper.jsscolab.domain.Modules.Messages.RoomIdResponse
import com.sandeveloper.jsscolab.domain.Modules.Messages.SendMessageRequest
import com.sandeveloper.jsscolab.domain.Modules.commonResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface MessageApi {

    @POST("/create-room")
    suspend fun createRoom(
        @Body createRoomRequest: Map<String,String>
    ): Response<RoomIdResponse>
    //mapOf("user_id" to user_id)

    @GET("/get-room-id")
    suspend fun getRoomId(
        @Query("user_id") user_id: String
    ): Response<RoomIdResponse>

    @GET("/get-messages")
    suspend fun getMessages(
        @Query("room_id") room_id: String,
    ): Response<GetMessagesResponse>

    @PUT("/send-message")
    suspend fun sendMessage(
        @Body sendMessageRequest: SendMessageRequest
    ): Response<commonResponse>

    @GET("/check-for-messages")
    suspend fun checkForMessages(
        @Query("room_id") room_id: String,
    ): Response<CheckForMessagesResponse>

    @DELETE("/delete-messages")
    suspend fun deleteMessages(
        @Query("room_id") room_id: String
    ): Response<commonResponse>
}