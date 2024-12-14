package com.sandeveloper.jsscolab.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sandeveloper.jsscolab.domain.Interfaces.ProfileRepository
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Profile.ReportUserRequest
import com.sandeveloper.jsscolab.domain.Modules.commonResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val reportRepository: ProfileRepository
):ViewModel() {

    private val _reportUser = MutableLiveData<ServerResult<commonResponse>>()
    public val reportUserResponse:LiveData<ServerResult<commonResponse>> = _reportUser

    fun reportUser(reportUserRequest: ReportUserRequest){
        viewModelScope.launch {
            reportRepository.reportUser(reportUserRequest){
                _reportUser.postValue(it)
            }
        }
    }
}