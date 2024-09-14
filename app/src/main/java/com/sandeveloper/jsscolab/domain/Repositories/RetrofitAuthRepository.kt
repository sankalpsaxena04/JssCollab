package com.sandeveloper.jsscolab.domain.Repositories

import android.media.tv.CommandResponse
import com.google.gson.JsonObject
import com.sandeveloper.jsscolab.domain.Api.AuthApi
import com.sandeveloper.jsscolab.domain.Api.ProfileApi
import com.sandeveloper.jsscolab.domain.HelperClasses.PrefManager
import com.sandeveloper.jsscolab.domain.Interfaces.AuthRepository
import com.sandeveloper.jsscolab.domain.Interfaces.ProfileRepository
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Auth.*
import com.sandeveloper.jsscolab.domain.Modules.Profile.*
import com.sandeveloper.jsscolab.domain.Modules.commonResponse
import retrofit2.Response
import javax.inject.Inject

class RetrofitAuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val profileApi: ProfileApi
) : AuthRepository, ProfileRepository {

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

    override suspend fun sendOtp(
        otpRequest: OtpRequest,
        serverResult: (ServerResult<OtpResponse>) -> Unit
    ) {
        handleApiResponse(
            apiCall = { authApi.sendOtp(otpRequest) },
            serverResult = serverResult,
            onSuccess = {
                if (it.success) {
                    PrefManager.setOtpOrderId(it.orderId.toString())
                }
            }
        )
    }

    override suspend fun resendOtp(
        orderId: String,
        serverResult: (ServerResult<OtpResponse>) -> Unit
    ) {
        handleApiResponse(
            apiCall = { authApi.resendOtp(mapOf("orderId" to orderId)) },
            serverResult = serverResult,
            onSuccess = {
                if (it.success) {
                    PrefManager.setOtpOrderId(it.orderId.toString())
                }
            }
        )
    }

    override suspend fun verifyOtp(
        phone: Long,
        otp: Int,
        orderId: String,
        serverResult: (ServerResult<OtpVerifiedResponse>) -> Unit
    ) {
        val jsonObject = JsonObject().apply {
            addProperty("phone", phone)
            addProperty("otp", otp)
            addProperty("orderId", orderId)
        }
        handleApiResponse(
            apiCall = { authApi.verifyOtp(jsonObject) },
            serverResult = serverResult,
            onSuccess = {
                if (it.success) {
                    PrefManager.setTempAuthToken(it.temp_token.toString())
                }
            }
        )
    }

    override suspend fun signUp(
        signupRequest: signupRequest,
        serverResult: (ServerResult<AuthResponse>) -> Unit
    ) {
        handleApiResponse(
            apiCall = { authApi.signup(signupRequest) },
            serverResult = serverResult,
            onSuccess = {
                if (it.success) {
                    PrefManager.setAuthToken(it.token.toString())
                }
            }
        )
    }

    override suspend fun login(
        loginRequest: LoginRequest,
        serverResult: (ServerResult<AuthResponse>) -> Unit
    ) {
        handleApiResponse(
            apiCall = { authApi.login(loginRequest) },
            serverResult = serverResult,
            onSuccess = {
                if (it.success) {
                    PrefManager.setAuthToken(it.token.toString())
                }
            }
        )
    }

    override suspend fun forgetPassword(
        forgetPasswordRequest: ForgetPasswordRequest,
        serverResult: (ServerResult<AuthResponse>) -> Unit
    ) {
        handleApiResponse(
            apiCall = { authApi.forgetPassword(forgetPasswordRequest) },
            serverResult = serverResult,
            onSuccess = {
                if (it.success) {
                    PrefManager.setAuthToken(it.token.toString())
                }
            }
        )
    }

    override suspend fun createProfile(
        createProfile: CreateProfile,
        serverResult: (ServerResult<commonResponse>) -> Unit
    ) {
        handleApiResponse(
            apiCall = { profileApi.createProfile(createProfile) },
            serverResult = serverResult
        )
    }

    override suspend fun updateProfile(
        profileUpdateRequest: ProfileUpdateRequest,
        serverResult: (ServerResult<commonResponse>) -> Unit
    ) {
        handleApiResponse(
            apiCall = { profileApi.updateProfile(profileUpdateRequest) },
            serverResult = serverResult
        )
    }

    override suspend fun updatePicture(
        pictureUpdateRequest: PictureUpdateRequest,
        serverResult: (ServerResult<commonResponse>) -> Unit
    ) {
        handleApiResponse(
            apiCall = { profileApi.updatePicture(pictureUpdateRequest) },
            serverResult = serverResult
        )
    }

    override suspend fun getMyDetails(
        serverResult: (ServerResult<MyProfileResponse>) -> Unit
    ) {
        handleApiResponse(
            apiCall = { profileApi.getMyDetails() },
            serverResult = serverResult
        )
    }

    override suspend fun getMyRating(
        serverResult: (ServerResult<MyRatingResponse>) -> Unit
    ) {
        handleApiResponse(
            apiCall = { profileApi.getMyRating() },
            serverResult = serverResult
        )
    }

    override suspend fun rateUser(
        rateUserRequest: RateUserRequest,
        serverResult: (ServerResult<commonResponse>) -> Unit
    ) {
        handleApiResponse(
            apiCall = { profileApi.rateUser(rateUserRequest) },
            serverResult = serverResult
        )
    }

    override suspend fun reportUser(
        reportUserRequest: ReportUserRequest,
        serverResult: (ServerResult<commonResponse>) -> Unit
    ) {
        handleApiResponse(
            apiCall = { profileApi.reportUser(reportUserRequest) },
            serverResult = serverResult
        )
    }

    override suspend fun blockUser(
        blockUserRequest: Map<String, String>,
        serverResult: (ServerResult<commonResponse>) -> Unit
    ) {
        handleApiResponse(
            apiCall = { profileApi.blockUser(blockUserRequest) },
            serverResult = serverResult
        )
    }

    override suspend fun deleteAccount(
        serverResult: (ServerResult<commonResponse>) -> Unit
    ) {
        handleApiResponse(
            apiCall = { profileApi.deleteAccount() },
            serverResult = serverResult
        )
    }

    override suspend fun isBanned(
        serverResult: (ServerResult<BanStatusResponse>) -> Unit
    ) {
        handleApiResponse(
            apiCall = { profileApi.isBanned() },
            serverResult = serverResult
        )
    }
}
