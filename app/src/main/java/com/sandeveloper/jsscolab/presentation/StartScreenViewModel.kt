package com.sandeveloper.jsscolab.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sandeveloper.jsscolab.domain.Interfaces.SwapRepository
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Profile.BanStatusResponse
import com.sandeveloper.jsscolab.domain.Modules.Profile.MyProfileResponse
import com.sandeveloper.jsscolab.domain.Modules.commonResponse
import com.sandeveloper.jsscolab.domain.Modules.swap.SwapEntity
import com.sandeveloper.jsscolab.domain.Modules.swap.SwapItem
import com.sandeveloper.jsscolab.domain.Modules.swap.swapItemResponse
import com.sandeveloper.jsscolab.domain.Repositories.RetrofitAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartScreenViewModel @Inject constructor(
   private val authRepository: RetrofitAuthRepository,
   private val swapRepository: SwapRepository,
    application: Application
) :AndroidViewModel(application) {

    private val _profileResponse = MutableLiveData<ServerResult<MyProfileResponse>>()
    val profileResponse: LiveData<ServerResult<MyProfileResponse>> = _profileResponse
    private val _banStatusResponse = MutableLiveData<ServerResult<BanStatusResponse>>()
    val banStatusResponse: LiveData<ServerResult<BanStatusResponse>> = _banStatusResponse
    private val _swapItemsResponse = MutableLiveData<ServerResult<swapItemResponse>>()
    val swapItemsResponse: LiveData<ServerResult<swapItemResponse>> = _swapItemsResponse
    private val _swapItemsDBResponse = MutableLiveData<ServerResult<commonResponse>>()
    val swapItemsDBResponse: LiveData<ServerResult<commonResponse>> = _swapItemsDBResponse

    private
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
    fun getSwapItems(){
        viewModelScope.launch {
            swapRepository.getSwapItems { result ->
                _swapItemsResponse.postValue(result)
            }
        }
    }

    fun setSwapsToDB(swaps:List<SwapEntity>){
        viewModelScope.launch {
            swapRepository.setSwapInDB(swaps){result->
                _swapItemsDBResponse.postValue(result)
            }
        }
    }

}