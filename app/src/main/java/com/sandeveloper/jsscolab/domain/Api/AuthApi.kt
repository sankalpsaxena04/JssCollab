package com.sandeveloper.jsscolab.domain.Api

import com.google.gson.JsonObject
import com.sandeveloper.jsscolab.domain.Modules.Auth.AuthResponse
import com.sandeveloper.jsscolab.domain.Modules.Auth.ForgetPasswordRequest
import com.sandeveloper.jsscolab.domain.Modules.Auth.LoginRequest
import com.sandeveloper.jsscolab.domain.Modules.Auth.OtpRequest
import com.sandeveloper.jsscolab.domain.Modules.Auth.OtpResponse
import com.sandeveloper.jsscolab.domain.Modules.Auth.OtpVerifiedResponse
import com.sandeveloper.jsscolab.domain.Modules.Auth.signupRequest
import com.sandeveloper.jsscolab.domain.Modules.commonResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/send-otp")
    // action="SIGNUP"
    suspend fun sendOtp(@Body authRequest: OtpRequest): Response<OtpResponse>

    @POST("auth/resend-otp")
    // val map = mapOf("orderId" to "value")
    suspend fun resendOtp(@Body authRequest: Map<String,String>): Response<OtpResponse>
    // gives success(bool), message, orderId (only if resent successfully)

    @POST("auth/verify-otp")
    //putin phone, otp, orderId
    suspend fun verifyOtp(@Body authRequest: JsonObject): Response<OtpVerifiedResponse>
    // gives success(bool), message, temp_token (only if verification successfully) to validate signup it is temp token

    @POST("auth/signup")
    suspend fun signup(@Body signupRequest: signupRequest): Response<AuthResponse>
    // gives success(bool), message, token (only if signup successfully)
    //use ProfileApi interface to add name admno etc


    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<AuthResponse>
    // gives success(bool), message, token (only if login successfully)

    @PATCH("auth/forget-password")
    suspend fun forgetPassword(@Body forgetPwdRequest: ForgetPasswordRequest):Response<AuthResponse>
    // gives success(bool), message, token if successful
}