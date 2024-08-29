package com.sandeveloper.jsscolab.domain.Api

import com.google.gson.JsonObject
import com.sandeveloper.jsscolab.domain.Modules.Auth.ForgetPasswordRequest
import com.sandeveloper.jsscolab.domain.Modules.Auth.LoginRequest
import com.sandeveloper.jsscolab.domain.Modules.Auth.OtpRequest
import com.sandeveloper.jsscolab.domain.Modules.Auth.signupRequest
import com.sandeveloper.jsscolab.domain.Modules.commonResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

interface AuthApi {

    @POST("/send-otp")
    // action="SIGNUP"
    suspend fun sendOtp(@Body authRequest: OtpRequest): Response<commonResponse>

    @POST("/resend-otp")
    // val map = mapOf("orderId" to "value")
    suspend fun resendOtp(@Body authRequest: Map<String,String>): Response<JsonObject>
    // gives success(bool), message, orderId (only if resent successfully)

    @POST("/verify-otp")
    //putin phone, otp, orderId
    suspend fun verifyOtp(@Body authRequest: JsonObject): Response<JsonObject>
    // gives success(bool), message, temp_token (only if verification successfully) to validate signup it is temp token

    @POST("/signup")
    suspend fun signup(@Body signupRequest: signupRequest): Response<JsonObject>
    // gives success(bool), message, token (only if signup successfully)
    //use ProfileApi interface to add name admno etc


    @POST("/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<JsonObject>
    // gives success(bool), message, token (only if login successfully)

    @PATCH("/forget-password")
    suspend fun forgetPassword(@Body forgetPwdRequest: ForgetPasswordRequest):Response<JsonObject>
    // gives success(bool), message, token if successful
}