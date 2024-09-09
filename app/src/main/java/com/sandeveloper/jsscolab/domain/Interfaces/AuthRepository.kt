package com.sandeveloper.jsscolab.domain.Interfaces

import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Auth.AuthResponse
import com.sandeveloper.jsscolab.domain.Modules.Auth.ForgetPasswordRequest
import com.sandeveloper.jsscolab.domain.Modules.Auth.LoginRequest
import com.sandeveloper.jsscolab.domain.Modules.Auth.OtpRequest
import com.sandeveloper.jsscolab.domain.Modules.Auth.OtpResponse
import com.sandeveloper.jsscolab.domain.Modules.Auth.OtpVerifiedResponse
import com.sandeveloper.jsscolab.domain.Modules.Auth.signupRequest
import com.sandeveloper.jsscolab.domain.Modules.commonResponse

interface AuthRepository {

    suspend fun sendOtp(otpRequest: OtpRequest,serverResult: (ServerResult<OtpResponse>)->Unit)
    suspend fun resendOtp(orderId:String,serverResult: (ServerResult<OtpResponse>) -> Unit)
    suspend fun verifyOtp(phone:Long,otp:Int,orderId:String,serverResult:(ServerResult<OtpVerifiedResponse>)->Unit)
    suspend fun signUp(signupRequest: signupRequest,serverResult: (ServerResult<AuthResponse>) -> Unit)
    suspend fun login(loginRequest: LoginRequest,serverResult: (ServerResult<AuthResponse>) -> Unit)
    suspend fun forgetPassword(forgetPasswordRequest: ForgetPasswordRequest, serverResult: (ServerResult<AuthResponse>) -> Unit)
}