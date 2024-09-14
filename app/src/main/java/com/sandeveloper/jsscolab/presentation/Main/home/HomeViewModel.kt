package com.sandeveloper.jsscolab.presentation.Main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sandeveloper.jsscolab.domain.Interfaces.PostRepository
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Post.createPost
import com.sandeveloper.jsscolab.domain.Modules.Post.getPostResponse
import com.sandeveloper.jsscolab.domain.Modules.Post.getPostsRequest
import com.sandeveloper.jsscolab.domain.Modules.Post.updatePostRequest
import com.sandeveloper.jsscolab.domain.Modules.commonResponse
import com.sandeveloper.jsscolab.domain.Modules.swap.createSwapRequest
import com.sandeveloper.jsscolab.domain.Modules.swap.getSwapResposne
import com.sandeveloper.jsscolab.domain.Modules.swap.getSwapsRequest
import com.sandeveloper.jsscolab.domain.Modules.swap.updateSwapRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _commonResponse = MutableLiveData<ServerResult<commonResponse>>()
    val commonResponse: LiveData<ServerResult<commonResponse>> = _commonResponse

    private val _coshopPostResponse = MutableLiveData<ServerResult<getPostResponse>>()
    val coshopPostResponse: LiveData<ServerResult<getPostResponse>> = _coshopPostResponse

    private val _swapPostResponse = MutableLiveData<ServerResult<getSwapResposne>>()
    val swapPostResponse: LiveData<ServerResult<getSwapResposne>> = _swapPostResponse

    /**
     * Create Co-shop Post
     */
    fun createCoshopPost(createPost: createPost) = viewModelScope.launch {
        _commonResponse.value = ServerResult.Progress
        postRepository.createCoshopPost(createPost) { result ->
            _commonResponse.postValue(result)
        }
    }

    /**
     * Get Co-shop Posts
     */
    fun getCoshopPosts(getPostsRequest: getPostsRequest) = viewModelScope.launch {
        _coshopPostResponse.value = ServerResult.Progress
        postRepository.getCoshopPosts(getPostsRequest) { result ->
            _coshopPostResponse.postValue(result)
        }
    }

    /**
     * Get My Co-shop Posts
     */
    fun getMyCoshopPosts() = viewModelScope.launch {
        _coshopPostResponse.value = ServerResult.Progress
        postRepository.getMyCoshopPosts { result ->
            _coshopPostResponse.postValue(result)
        }
    }

    /**
     * Update Co-shop Post
     */
    fun updateCoshopPost(updatePostRequest: updatePostRequest) = viewModelScope.launch {
        _commonResponse.value = ServerResult.Progress
        postRepository.updateCoshopPost(updatePostRequest) { result ->
            _commonResponse.postValue(result)
        }
    }

    /**
     * Delete Co-shop Post
     */
    fun deleteCoshopPost(postId: String) = viewModelScope.launch {
        _commonResponse.value = ServerResult.Progress
        postRepository.deleteCoshopPost(postId) { result ->
            _commonResponse.postValue(result)
        }
    }

    /**
     * Create Swap Post
     */
    fun createSwap(createSwapRequest: createSwapRequest) = viewModelScope.launch {
        _commonResponse.value = ServerResult.Progress
        postRepository.createSwap(createSwapRequest) { result ->
            _commonResponse.postValue(result)
        }
    }

    /**
     * Get Swap Posts
     */
    fun getSwaps(getSwapsRequest: getSwapsRequest) = viewModelScope.launch {
        _swapPostResponse.value = ServerResult.Progress
        postRepository.getSwaps(getSwapsRequest) { result ->
            _swapPostResponse.postValue(result)
        }
    }

    /**
     * Get My Swap Posts
     */
    fun getMySwaps() = viewModelScope.launch {
        _swapPostResponse.value = ServerResult.Progress
        postRepository.getMySwaps { result ->
            _swapPostResponse.postValue(result)
        }
    }

    /**
     * Update Swap Post
     */
    fun updateSwap(updateSwapRequest: updateSwapRequest) = viewModelScope.launch {
        _commonResponse.value = ServerResult.Progress
        postRepository.updateSwap(updateSwapRequest) { result ->
            _commonResponse.postValue(result)
        }
    }

    /**
     * Delete Swap Post
     */
    fun deleteSwap(swapId: String) = viewModelScope.launch {
        _commonResponse.value = ServerResult.Progress
        postRepository.deleteSwap(swapId) { result ->
            _commonResponse.postValue(result)
        }
    }
}
