package com.sandeveloper.jsscolab.presentation.Main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.sandeveloper.jsscolab.data.Room.SwapDAO
import com.sandeveloper.jsscolab.domain.Interfaces.AppsRepository
import com.sandeveloper.jsscolab.domain.Interfaces.SwapRepository
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Post.App
import com.sandeveloper.jsscolab.domain.Modules.Post.createPost
import com.sandeveloper.jsscolab.domain.Modules.Post.updatePostRequest
import com.sandeveloper.jsscolab.domain.Modules.commonResponse
import com.sandeveloper.jsscolab.domain.Modules.swap.SwapEntity
import com.sandeveloper.jsscolab.domain.Modules.swap.createSwapRequest
import com.sandeveloper.jsscolab.domain.Modules.swap.updateSwapRequest
import com.sandeveloper.jsscolab.domain.Repositories.RetrofitPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditViewModel  @Inject constructor(
    private val coshopRepository: RetrofitPostRepository,
    private val swapDao: SwapDAO,
    private val swapApi: SwapRepository,
    private val appRepository: AppsRepository
) : ViewModel() {
    private val _getAppsByCategoryResponse = MutableLiveData<List<App>>()
    val getAppsByNameCategoryResponse: LiveData<List<App>> = _getAppsByCategoryResponse

    private val _createPostState = MutableLiveData<ServerResult<commonResponse>>()
    val createPostState: MutableLiveData<ServerResult<commonResponse>> = _createPostState

    fun getAppsByCategory(category:String){
        viewModelScope.launch {
            val result = appRepository.getAppsNameFrom(category)
            _getAppsByCategoryResponse.value = result
        }
    }
    fun updatePost(updatePostRequest: updatePostRequest) {
        viewModelScope.launch {
            coshopRepository.updateCoshopPost(updatePostRequest) { serverResult ->
                _createPostState.value = serverResult
            }
        }
    }
    fun updateSwap(updateSwap: updateSwapRequest){
        viewModelScope.launch {
            swapApi.updateSwap(updateSwap){
                _createPostState.value = it
            }
        }
    }
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun getSearchQuery(query: String) {
        _searchQuery.value = query
    }
    private val _searchQuery = MutableLiveData<String>()
    val searchResults: LiveData<List<SwapEntity>> = _searchQuery.switchMap { query ->
        swapDao.searchSwapItems(query)

    }

}