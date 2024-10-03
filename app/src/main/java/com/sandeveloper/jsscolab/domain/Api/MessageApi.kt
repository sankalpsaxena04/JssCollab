package com.sandeveloper.jsscolab.domain.Api

import com.sandeveloper.jsscolab.domain.Modules.Messages.CheckForMessagesResponse
import com.sandeveloper.jsscolab.domain.Modules.Messages.GetMessagesResponse
import com.sandeveloper.jsscolab.domain.Modules.Messages.RoomIdResponse
import com.sandeveloper.jsscolab.domain.Modules.Messages.SendMessageRequest
import com.sandeveloper.jsscolab.domain.Modules.Messages.userProfileResponse
import com.sandeveloper.jsscolab.domain.Modules.commonResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface MessageApi {

    //mapOf("user_id" to user_id)

    @POST("messages/get-room-id")
    suspend fun getRoomId(
        @Body createRoomRequest: Map<String,String>
    ): Response<RoomIdResponse>

    @GET("messages/get-messages")
    suspend fun getMessages(
        @Query("room_id") room_id: String,
    ): Response<GetMessagesResponse>

    @PUT("messages/send-message")
    suspend fun sendMessage(
        @Body sendMessageRequest: SendMessageRequest
    ): Response<commonResponse>

    @GET("messages/check-for-messages")
    suspend fun checkForMessages(
        @Query("room_id") room_id: String,
    ): Response<CheckForMessagesResponse>

    @GET("messages/get-user-profile")
    suspend fun getUserProfile( @Query("room_id") room_id: String, @Query("user_id") userId:String):Response<userProfileResponse>

    @DELETE("messages/delete-messages")
    suspend fun deleteMessages(
        @Query("room_id") room_id: String
    ): Response<commonResponse>
}