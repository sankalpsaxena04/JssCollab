package com.sandeveloper.jsscolab.domain.Api

import com.sandeveloper.jsscolab.domain.Modules.Profile.BanStatusResponse
import com.sandeveloper.jsscolab.domain.Modules.Profile.CreateProfile
import com.sandeveloper.jsscolab.domain.Modules.Profile.MyProfileResponse
import com.sandeveloper.jsscolab.domain.Modules.Profile.MyRatingResponse
import com.sandeveloper.jsscolab.domain.Modules.Profile.PictureUpdateRequest
import com.sandeveloper.jsscolab.domain.Modules.Profile.ProfileUpdateRequest
import com.sandeveloper.jsscolab.domain.Modules.Profile.RateUserRequest
import com.sandeveloper.jsscolab.domain.Modules.Profile.ReportUserRequest
import com.sandeveloper.jsscolab.domain.Modules.commonResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT

interface ProfileApi {
    @POST("profile/create-profile")
    suspend fun createProfile(@Body createProfile: CreateProfile): Response<commonResponse>

    @PATCH("profile/update-profile")
    suspend fun updateProfile(
        @Body profileUpdateRequest: ProfileUpdateRequest
    ): Response<commonResponse>

    @PUT("profile/update-picture")
    suspend fun updatePicture(
        @Body pictureUpdateRequest: PictureUpdateRequest
    ): Response<commonResponse>

    @GET("profile/my-details")
    suspend fun getMyDetails(): Response<MyProfileResponse>

    @GET("profile/my-rating")
    suspend fun getMyRating(): Response<MyRatingResponse>

    @POST("profile/rate-user")
    suspend fun rateUser(
        @Body rateUserRequest: RateUserRequest
    ): Response<commonResponse>

    @POST("profile/report-user")
    suspend fun reportUser(
        @Body reportUserRequest: ReportUserRequest
    ): Response<commonResponse>

    @POST("profile/block-user")
    suspend fun blockUser(
        @Body blockUserRequest: Map<String,String>
    ): Response<commonResponse>
    // mapOf("user_id" to user_id)

    @DELETE("profile/delete-account")
    suspend fun deleteAccount(): Response<commonResponse>

    @GET("profile/is-banned")
    suspend fun isBanned(): Response<BanStatusResponse>


}