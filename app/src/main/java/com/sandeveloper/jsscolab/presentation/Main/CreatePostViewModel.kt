package com.sandeveloper.jsscolab.presentation.Main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sandeveloper.jsscolab.domain.Interfaces.PostRepository
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Post.createPost
import com.sandeveloper.jsscolab.domain.Modules.commonResponse
import com.sandeveloper.jsscolab.domain.Repositories.RetrofitPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val coshopRepository: RetrofitPostRepository
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

}