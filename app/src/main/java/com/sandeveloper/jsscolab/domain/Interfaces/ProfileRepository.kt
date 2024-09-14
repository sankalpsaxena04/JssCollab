package com.sandeveloper.jsscolab.domain.Interfaces

import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Profile.BanStatusResponse
import com.sandeveloper.jsscolab.domain.Modules.Profile.CreateProfile
import com.sandeveloper.jsscolab.domain.Modules.Profile.MyProfileResponse
import com.sandeveloper.jsscolab.domain.Modules.Profile.MyRatingResponse
import com.sandeveloper.jsscolab.domain.Modules.Profile.PictureUpdateRequest
import com.sandeveloper.jsscolab.domain.Modules.Profile.ProfileUpdateRequest
import com.sandeveloper.jsscolab.domain.Modules.Profile.RateUserRequest
import com.sandeveloper.jsscolab.domain.Modules.Profile.ReportUserRequest
import com.sandeveloper.jsscolab.domain.Modules.commonResponse

interface ProfileRepository {

    suspend fun createProfile(
        createProfile: CreateProfile,
        serverResult: (ServerResult<commonResponse>) -> Unit
    )

    suspend fun updateProfile(
        profileUpdateRequest: ProfileUpdateRequest,
        serverResult: (ServerResult<commonResponse>) -> Unit
    )

    suspend fun updatePicture(
        pictureUpdateRequest: PictureUpdateRequest,
        serverResult: (ServerResult<commonResponse>) -> Unit
    )

    suspend fun getMyDetails(
        serverResult: (ServerResult<MyProfileResponse>) -> Unit
    )

    suspend fun getMyRating(
        serverResult: (ServerResult<MyRatingResponse>) -> Unit
    )

    suspend fun rateUser(
        rateUserRequest: RateUserRequest,
        serverResult: (ServerResult<commonResponse>) -> Unit
    )

    suspend fun reportUser(
        reportUserRequest: ReportUserRequest,
        serverResult: (ServerResult<commonResponse>) -> Unit
    )

    suspend fun blockUser(
        blockUserRequest: Map<String, String>,
        serverResult: (ServerResult<commonResponse>) -> Unit
    )

    suspend fun deleteAccount(
        serverResult: (ServerResult<commonResponse>) -> Unit
    )

    suspend fun isBanned(
        serverResult: (ServerResult<BanStatusResponse>) -> Unit
    )

}
