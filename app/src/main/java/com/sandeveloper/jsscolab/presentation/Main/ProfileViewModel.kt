package com.sandeveloper.jsscolab.presentation.Main

import android.Manifest
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
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
import com.sandeveloper.jsscolab.domain.Repositories.RetrofitAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: RetrofitAuthRepository
):ViewModel() {

    private val _createProfileResponse = MutableLiveData<ServerResult<commonResponse>>()
    val createProfileResponse: LiveData<ServerResult<commonResponse>> = _createProfileResponse

    private val _updateProfilePictureResponse =  MutableLiveData<ServerResult<commonResponse>>()
    val updateProfilePictureResponse: LiveData<ServerResult<commonResponse>> = _updateProfilePictureResponse

    private val _profileResponse = MutableLiveData<ServerResult<MyProfileResponse>>()
    val profileResponse: LiveData<ServerResult<MyProfileResponse>> = _profileResponse

    private val _ratingResponse = MutableLiveData<ServerResult<MyRatingResponse>>()
    val ratingResponse: LiveData<ServerResult<MyRatingResponse>> = _ratingResponse

    private val _commonResponse = MutableLiveData<ServerResult<commonResponse>>()
    val commonResponse: LiveData<ServerResult<commonResponse>> = _commonResponse

    private val _banStatusResponse = MutableLiveData<ServerResult<BanStatusResponse>>()
    val banStatusResponse: LiveData<ServerResult<BanStatusResponse>> = _banStatusResponse

    private val _deleteAccountResponse = MutableLiveData<ServerResult<commonResponse>>()
    val deleteAccountResponse: LiveData<ServerResult<commonResponse>> = _deleteAccountResponse

    private val _blockUserResponse = MutableLiveData<ServerResult<commonResponse>>()
    val blockUserResponse: LiveData<ServerResult<commonResponse>> = _blockUserResponse

    private val _reportUserResponse = MutableLiveData<ServerResult<commonResponse>>()
    val reportUserResponse: LiveData<ServerResult<commonResponse>> = _reportUserResponse
    private val _setFCMresponse = MutableLiveData<ServerResult<commonResponse>>()
    val setFCMResponse :LiveData<ServerResult<commonResponse>> = _setFCMresponse


    // Create Profile
    fun createProfile(createProfile: CreateProfile) {
        viewModelScope.launch {
            authRepository.createProfile(createProfile) { result ->
                _createProfileResponse.postValue(result)
            }
        }
    }

    // Update Profile
    fun updateProfile(profileUpdateRequest: ProfileUpdateRequest) {
        viewModelScope.launch {
            authRepository.updateProfile(profileUpdateRequest) { result ->
                _commonResponse.postValue(result)
            }
        }
    }

    // Update Picture
    fun updatePicture(pictureUpdateRequest: PictureUpdateRequest) {
        viewModelScope.launch {
            authRepository.updatePicture(pictureUpdateRequest) { result ->
                _updateProfilePictureResponse.postValue(result)
            }
        }
    }

    // Get My Details
    fun getMyDetails() {
        viewModelScope.launch {
            authRepository.getMyDetails { result ->
                _profileResponse.postValue(result)
            }
        }
    }

    // Get My Rating
    fun getMyRating() {
        viewModelScope.launch {
            authRepository.getMyRating { result ->
                _ratingResponse.postValue(result)
            }
        }
    }

    // Rate User
    fun rateUser(rateUserRequest: RateUserRequest) {
        viewModelScope.launch {
            authRepository.rateUser(rateUserRequest) { result ->
                _commonResponse.postValue(result)
            }
        }
    }

    // Report User
    fun reportUser(reportUserRequest: ReportUserRequest) {
        viewModelScope.launch {
            authRepository.reportUser(reportUserRequest) { result ->
                _reportUserResponse.postValue(result)
            }
        }
    }

    // Block User
    fun blockUser(blockUserRequest: Map<String, String>) {
        viewModelScope.launch {
            authRepository.blockUser(blockUserRequest) { result ->
                _blockUserResponse.postValue(result)
            }
        }
    }

    // Delete Account
    fun deleteAccount() {
        viewModelScope.launch {
            authRepository.deleteAccount { result ->
                _deleteAccountResponse.postValue(result)
            }
        }
    }

    // Check if user is banned
    fun isBanned() {
        viewModelScope.launch {
            authRepository.isBanned { result ->
                _banStatusResponse.postValue(result)
            }
        }
    }

    fun setFCM(fcmToken:Map<String,String>){
        viewModelScope.launch {
            authRepository.setFcm(fcmToken){
                _setFCMresponse.postValue(it)
            }
        }
    }
}
