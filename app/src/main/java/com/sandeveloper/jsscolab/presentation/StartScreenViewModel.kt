package com.sandeveloper.jsscolab.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Profile.BanStatusResponse
import com.sandeveloper.jsscolab.domain.Modules.Profile.MyProfileResponse
import com.sandeveloper.jsscolab.domain.Repositories.RetrofitAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartScreenViewModel @Inject constructor(
   private val authRepository: RetrofitAuthRepository,
    application: Application
) :AndroidViewModel(application) {

    private val _profileResponse = MutableLiveData<ServerResult<MyProfileResponse>>()
    val profileResponse: LiveData<ServerResult<MyProfileResponse>> = _profileResponse
    private val _banStatusResponse = MutableLiveData<ServerResult<BanStatusResponse>>()
    val banStatusResponse: LiveData<ServerResult<BanStatusResponse>> = _banStatusResponse
    companion object {
        val TAG = "StartScreenViewModel"
    }
    fun getMyDetails() {
        viewModelScope.launch {
            authRepository.getMyDetails { result ->
                _profileResponse.postValue(result)
            }
        }
    }
    fun isBanned() {
        viewModelScope.launch {
            authRepository.isBanned { result ->
                _banStatusResponse.postValue(result)
            }
        }
    }

}