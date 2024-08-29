package com.sandeveloper.jsscolab.domain.Api

import com.google.gson.JsonObject
import com.sandeveloper.jsscolab.BuildConfig
//import com.sandeveloper.jsscolab.BuildConfig
import retrofit2.Response

import retrofit2.http.Body
import retrofit2.http.Headers

import retrofit2.http.POST


interface NotificationApiService {
    // get FCM server key from Build Configs TODO
    //                               V here
    @Headers("Authorization: key=" + "", "Content-Type: application/json")
    @POST("fcm/send")
   suspend fun sendNotification(@Body payload: JsonObject?): Response<JsonObject>
}