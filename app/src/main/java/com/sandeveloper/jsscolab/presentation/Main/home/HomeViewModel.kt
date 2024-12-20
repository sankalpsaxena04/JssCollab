package com.sandeveloper.jsscolab.presentation.Main.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sandeveloper.jsscolab.domain.Interfaces.PostRepository
import com.sandeveloper.jsscolab.domain.Interfaces.SwapRepository
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Post.Posts
import com.sandeveloper.jsscolab.domain.Modules.Post.createPost
import com.sandeveloper.jsscolab.domain.Modules.Post.getPostResponse
import com.sandeveloper.jsscolab.domain.Modules.Post.getPostsRequest
import com.sandeveloper.jsscolab.domain.Modules.Post.updatePostRequest
import com.sandeveloper.jsscolab.domain.Modules.commonResponse
import com.sandeveloper.jsscolab.domain.Modules.swap.createSwapRequest
import com.sandeveloper.jsscolab.domain.Modules.swap.getSwapResposne
import com.sandeveloper.jsscolab.domain.Modules.swap.getSwapsRequest
import com.sandeveloper.jsscolab.domain.Modules.swap.updateSwapRequest
import com.sandeveloper.jsscolab.domain.Repositories.RetrofitPostRepository
import com.sandeveloper.jsscolab.domain.Utility.ExtensionsUtil.isNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val postRepository: RetrofitPostRepository,
    private val swapRepository: SwapRepository
):ViewModel(){

    private val _commonResponse = MutableLiveData<ServerResult<commonResponse>>()
    val commonResponse: LiveData<ServerResult<commonResponse>> = _commonResponse

    private val _coshopPostResponse = MutableLiveData<ServerResult<getPostResponse>>()
    val coshopPostResponse: LiveData<ServerResult<getPostResponse>> = _coshopPostResponse

    private val _swapPostResponse = MutableLiveData<ServerResult<getSwapResposne>>()
    val swapPostResponse: LiveData<ServerResult<getSwapResposne>> = _swapPostResponse


    private var cachedPosts:getPostResponse?=null

    fun getCoshopPosts(getPostsRequest: getPostsRequest) = viewModelScope.launch {
        // Check if posts are already cached
//        if (!cachedPosts.isNull) {
//            _coshopPostResponse.postValue(ServerResult.Success(cachedPosts!!))
//            return@launch
//        }

        // Show progress and fetch data from repository
        _coshopPostResponse.value = ServerResult.Progress
        postRepository.getCoshopPosts(getPostsRequest) { result ->
            when (result) {
                is ServerResult.Success -> {
                    _coshopPostResponse.postValue(ServerResult.Success(result.data))
                }
                is ServerResult.Failure -> {
                    _coshopPostResponse.postValue(ServerResult.Failure(result.exception))
                }
                else -> {
                    _coshopPostResponse.postValue(result)
                }
            }
        }
    }
    fun getSwapName(id:String){
        viewModelScope.launch {

            swapRepository.getSwapNameFrom(id)
        }
    }

    fun getMyCoshopPosts() = viewModelScope.launch {
        _coshopPostResponse.value = ServerResult.Progress
        postRepository.getMyCoshopPosts { result ->
            when (result) {
                is ServerResult.Success -> {
                    _coshopPostResponse.postValue(ServerResult.Success(result.data))
                }
                is ServerResult.Failure -> {
                    _coshopPostResponse.postValue(ServerResult.Failure(result.exception))
                }
                else -> {
                    _coshopPostResponse.postValue(result)
                }
            }
        }
    }
//
//    fun updateCoshopPost(updatePostRequest: updatePostRequest) = viewModelScope.launch {
//        _commonResponse.value = ServerResult.Progress
//        postRepository.updateCoshopPost(updatePostRequest) { result ->
//            when (result) {
//                is ServerResult.Success -> {
//                    _commonResponse.postValue(ServerResult.Success(result.data))
//                }
//                is ServerResult.Failure -> {
//                    _commonResponse.postValue(ServerResult.Failure(result.exception))
//                }
//                else -> {
//                    _commonResponse.postValue(result)
//                }
//            }
//        }
//    }
//
    fun deleteCoshopPost(postId: String) = viewModelScope.launch {
        _commonResponse.value = ServerResult.Progress
        postRepository.deleteCoshopPost(postId) { result ->
            when (result) {
                is ServerResult.Success -> {
                    _commonResponse.postValue(ServerResult.Success(result.data))
                }
                is ServerResult.Failure -> {
                    _commonResponse.postValue(ServerResult.Failure(result.exception))
                }
                else -> {
                    _commonResponse.postValue(result)
                }
            }
        }
    }
//
//    fun createSwap(createSwapRequest: createSwapRequest) = viewModelScope.launch {
//        _commonResponse.value = ServerResult.Progress
//        swapRepository.createSwap(createSwapRequest) { result ->
//            when (result) {
//                is ServerResult.Success -> {
//                    _commonResponse.postValue(ServerResult.Success(result.data))
//                }
//                is ServerResult.Failure -> {
//                    _commonResponse.postValue(ServerResult.Failure(result.exception))
//                }
//                else -> {
//                    _commonResponse.postValue(result)
//                }
//            }
//        }
//    }

    fun getSwaps(getSwapsRequest: getSwapsRequest) = viewModelScope.launch {
        _swapPostResponse.value = ServerResult.Progress
        swapRepository.getSwaps(getSwapsRequest) { result ->
            when (result) {
                is ServerResult.Success -> {
                    _swapPostResponse.postValue(ServerResult.Success(result.data))
                }
                is ServerResult.Failure -> {
                    _swapPostResponse.postValue(ServerResult.Failure(result.exception))
                }
                is ServerResult.Progress ->{
                    _swapPostResponse.postValue(ServerResult.Progress)
                }
            }
        }
    }

//    fun getMySwaps() = viewModelScope.launch {
//        _swapPostResponse.value = ServerResult.Progress
//        swapRepository.getMySwaps { result ->
//            when (result) {
//                is ServerResult.Success -> {
//                    _swapPostResponse.postValue(ServerResult.Success(result.data))
//                }
//                is ServerResult.Failure -> {
//                    _swapPostResponse.postValue(ServerResult.Failure(result.exception))
//                }
//                else -> {
//                    _swapPostResponse.postValue(result)
//                }
//            }
//        }
//    }
//
//    fun updateSwap(updateSwapRequest: updateSwapRequest) = viewModelScope.launch {
//        _commonResponse.value = ServerResult.Progress
//        swapRepository.updateSwap(updateSwapRequest) { result ->
//            when (result) {
//                is ServerResult.Success -> {
//                    _commonResponse.postValue(ServerResult.Success(result.data))
//                }
//                is ServerResult.Failure -> {
//                    _commonResponse.postValue(ServerResult.Failure(result.exception))
//                }
//                else -> {
//                    _commonResponse.postValue(result)
//                }
//            }
//        }
//    }
//
//    fun deleteSwap(swapId: String) = viewModelScope.launch {
//        _commonResponse.value = ServerResult.Progress
//        swapRepository.deleteSwap(swapId) { result ->
//            when (result) {
//                is ServerResult.Success -> {
//                    _commonResponse.postValue(ServerResult.Success(result.data))
//                }
//                is ServerResult.Failure -> {
//                    _commonResponse.postValue(ServerResult.Failure(result.exception))
//                }
//                else -> {
//                    _commonResponse.postValue(result)
//                }
//            }
//        }
//    }
}
