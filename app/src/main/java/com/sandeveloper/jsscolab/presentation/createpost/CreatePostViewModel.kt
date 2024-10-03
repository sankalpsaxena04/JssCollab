package com.sandeveloper.jsscolab.presentation.createpost

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.sandeveloper.jsscolab.data.Room.SwapDAO
import com.sandeveloper.jsscolab.domain.Api.SwapApi
import com.sandeveloper.jsscolab.domain.Interfaces.SwapRepository
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Post.createPost
import com.sandeveloper.jsscolab.domain.Modules.commonResponse
import com.sandeveloper.jsscolab.domain.Modules.swap.SwapEntity
import com.sandeveloper.jsscolab.domain.Modules.swap.createSwapRequest
import com.sandeveloper.jsscolab.domain.Repositories.RetrofitPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val coshopRepository: RetrofitPostRepository,
    private val swapDao: SwapDAO,
    private val swapApi: SwapRepository
) : ViewModel() {
    private val _createPostState = MutableLiveData<ServerResult<commonResponse>>()
    val createPostState: MutableLiveData<ServerResult<commonResponse>> = _createPostState

    fun createCoshopPost(createPost: createPost) {
        viewModelScope.launch {
            coshopRepository.createCoshopPost(createPost) { serverResult ->
                _createPostState.value = serverResult
            }
        }
    }
    private val _searchQuery = MutableLiveData<String>()
    val searchResults: LiveData<List<SwapEntity>> = _searchQuery.switchMap { query ->
        swapDao.searchSwapItems(query)

    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
    private val _giveQuery = MutableLiveData<String>()
    val getSearchResults: LiveData<List<SwapEntity>> = _giveQuery.switchMap { query ->
        swapDao.searchgiveSwapItems(query)
    }

    fun getSearchQuery(query: String) {
        _searchQuery.value = query
    }
    fun createSwap(createSwapRequest: createSwapRequest){
        viewModelScope.launch {
            swapApi.createSwap(createSwapRequest){
                _createPostState.value = it
            }
        }

    }
}