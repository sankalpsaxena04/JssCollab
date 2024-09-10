package com.sandeveloper.jsscolab.domain.Api

import com.google.gson.JsonObject
import com.sandeveloper.jsscolab.domain.Modules.app.getAppResponse
import com.sandeveloper.jsscolab.domain.Modules.app.getInfoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface appsApi {

    @GET("/get-names")
    suspend fun getNames():Response<getAppResponse>

    @GET("get-all-apps")
    suspend fun getAllApps(
        @Query("limit") limit: Int?,
        @Query("category") category: String?
    ): Response<getInfoResponse>
}