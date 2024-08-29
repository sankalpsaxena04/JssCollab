package com.sandeveloper.jsscolab.domain.Api

import com.google.gson.JsonObject
import com.sandeveloper.jsscolab.domain.Modules.Profile.CreateProfile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ProfileApi {
    @POST("/create-profile")
    suspend fun createProfile(@Body createProfile: CreateProfile): Response<JsonObject>
}