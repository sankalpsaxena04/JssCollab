package com.sandeveloper.jsscolab.presentation.Main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sandeveloper.jsscolab.domain.Api.ProfileApi
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileApi: ProfileApi
) : ViewModel() {

    private val _profileResponse = MutableLiveData<ServerResult<MyProfileResponse>>()
    val profileResponse: LiveData<ServerResult<MyProfileResponse>> = _profileResponse

    private val _ratingResponse = MutableLiveData<ServerResult<MyRatingResponse>>()
    val ratingResponse: LiveData<ServerResult<MyRatingResponse>> = _ratingResponse

    private val _commonResponse = MutableLiveData<ServerResult<commonResponse>>()
    val commonResponse: LiveData<ServerResult<commonResponse>> = _commonResponse

    private val _banStatusResponse = MutableLiveData<ServerResult<BanStatusResponse>>()
    val banStatusResponse: LiveData<ServerResult<BanStatusResponse>> = _banStatusResponse

    /**
     * Create profile API call
     */
    fun createProfile(createProfile: CreateProfile) = viewModelScope.launch {
        _commonResponse.value = ServerResult.Progress
        try {
            val response = profileApi.createProfile(createProfile)
            if (response.isSuccessful) {
                _commonResponse.value = ServerResult.Success(response.body()!!)
            } else {
                _commonResponse.value = ServerResult.Failure(Exception("Failed to create profile"))
            }
        } catch (e: Exception) {
            _commonResponse.value = ServerResult.Failure(e)
        }
    }

    /**
     * Update profile API call
     */
    fun updateProfile(profileUpdateRequest: ProfileUpdateRequest) = viewModelScope.launch {
        _commonResponse.value = ServerResult.Progress
        try {
            val response = profileApi.updateProfile(profileUpdateRequest)
            if (response.isSuccessful) {
                _commonResponse.value = ServerResult.Success(response.body()!!)
            } else {
                _commonResponse.value = ServerResult.Failure(Exception("Failed to update profile"))
            }
        } catch (e: Exception) {
            _commonResponse.value = ServerResult.Failure(e)
        }
    }

    /**
     * Update profile picture API call
     */
    fun updatePicture(pictureUpdateRequest: PictureUpdateRequest) = viewModelScope.launch {
        _commonResponse.value = ServerResult.Progress
        try {
            val response = profileApi.updatePicture(pictureUpdateRequest)
            if (response.isSuccessful) {
                _commonResponse.value = ServerResult.Success(response.body()!!)
            } else {
                _commonResponse.value = ServerResult.Failure(Exception("Failed to update picture"))
            }
        } catch (e: Exception) {
            _commonResponse.value = ServerResult.Failure(e)
        }
    }

    /**
     * Get my details API call
     */
    fun getMyDetails() = viewModelScope.launch {
        _profileResponse.value = ServerResult.Progress
        try {
            val response = profileApi.getMyDetails()
            if (response.isSuccessful) {
                _profileResponse.value = ServerResult.Success(response.body()!!)
            } else {
                _profileResponse.value = ServerResult.Failure(Exception("Failed to fetch profile details"))
            }
        } catch (e: Exception) {
            _profileResponse.value = ServerResult.Failure(e)
        }
    }

    /**
     * Get my rating API call
     */
    fun getMyRating() = viewModelScope.launch {
        _ratingResponse.value = ServerResult.Progress
        try {
            val response = profileApi.getMyRating()
            if (response.isSuccessful) {
                _ratingResponse.value = ServerResult.Success(response.body()!!)
            } else {
                _ratingResponse.value = ServerResult.Failure(Exception("Failed to fetch rating"))
            }
        } catch (e: Exception) {
            _ratingResponse.value = ServerResult.Failure(e)
        }
    }

    /**
     * Rate a user API call
     */
    fun rateUser(rateUserRequest: RateUserRequest) = viewModelScope.launch {
        _commonResponse.value = ServerResult.Progress
        try {
            val response = profileApi.rateUser(rateUserRequest)
            if (response.isSuccessful) {
                _commonResponse.value = ServerResult.Success(response.body()!!)
            } else {
                _commonResponse.value = ServerResult.Failure(Exception("Failed to rate user"))
            }
        } catch (e: Exception) {
            _commonResponse.value = ServerResult.Failure(e)
        }
    }

    /**
     * Report a user API call
     */
    fun reportUser(reportUserRequest: ReportUserRequest) = viewModelScope.launch {
        _commonResponse.value = ServerResult.Progress
        try {
            val response = profileApi.reportUser(reportUserRequest)
            if (response.isSuccessful) {
                _commonResponse.value = ServerResult.Success(response.body()!!)
            } else {
                _commonResponse.value = ServerResult.Failure(Exception("Failed to report user"))
            }
        } catch (e: Exception) {
            _commonResponse.value = ServerResult.Failure(e)
        }
    }

    /**
     * Block a user API call
     */
    fun blockUser(userId: String) = viewModelScope.launch {
        _commonResponse.value = ServerResult.Progress
        try {
            val response = profileApi.blockUser(mapOf("user_id" to userId))
            if (response.isSuccessful) {
                _commonResponse.value = ServerResult.Success(response.body()!!)
            } else {
                _commonResponse.value = ServerResult.Failure(Exception("Failed to block user"))
            }
        } catch (e: Exception) {
            _commonResponse.value = ServerResult.Failure(e)
        }
    }

    /**
     * Delete account API call
     */
    fun deleteAccount() = viewModelScope.launch {
        _commonResponse.value = ServerResult.Progress
        try {
            val response = profileApi.deleteAccount()
            if (response.isSuccessful) {
                _commonResponse.value = ServerResult.Success(response.body()!!)
            } else {
                _commonResponse.value = ServerResult.Failure(Exception("Failed to delete account"))
            }
        } catch (e: Exception) {
            _commonResponse.value = ServerResult.Failure(e)
        }
    }

    /**
     * Check if user is banned API call
     */
    fun isBanned() = viewModelScope.launch {
        _banStatusResponse.value = ServerResult.Progress
        try {
            val response = profileApi.isBanned()
            if (response.isSuccessful) {
                _banStatusResponse.value = ServerResult.Success(response.body()!!)
            } else {
                _banStatusResponse.value = ServerResult.Failure(Exception("Failed to check ban status"))
            }
        } catch (e: Exception) {
            _banStatusResponse.value = ServerResult.Failure(e)
        }
    }
}
