package com.sandeveloper.jsscolab.presentation.Main.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sandeveloper.jsscolab.domain.Interfaces.MessageRepository
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Messages.Message
import com.sandeveloper.jsscolab.domain.Modules.Messages.MessageEntity
import com.sandeveloper.jsscolab.domain.Modules.Messages.SendMessageRequest
import com.sandeveloper.jsscolab.domain.Repositories.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> = _messages

    private val _newMessage = MutableLiveData<Message>()
    val newMessage: LiveData<Message> = _newMessage

    private val _roomId = MutableLiveData<String>()
    val roomId: LiveData<String> = _roomId

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun initializeSocket() {
        chatRepository.initializeSocket()
    }

    fun connectSocket() {
        chatRepository.connectSocket()
    }

    fun disconnectSocket() {
        chatRepository.disconnectSocket()
    }

    fun listenForMessages() {
        chatRepository.listenForMessages { result ->
            when (result) {
                is ServerResult.Success -> {
                    _newMessage.postValue(result.data)
                }
                is ServerResult.Failure -> {
                    _error.postValue(result.exception.message)
                }
                else -> {}
            }
        }
    }

    fun fetchRoomId(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.getRoomId(userId) { result ->
                when (result) {
                    is ServerResult.Success -> {
                        _roomId.postValue(result.data.room!!)
                    }
                    is ServerResult.Failure -> {
                        _error.postValue(result.exception.message)
                    }
                    else -> {}
                }
            }
        }
    }

    fun fetchMessages(roomId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.postValue(true)
            chatRepository.getMessages(roomId) { result ->
                _loading.postValue(false)
                when (result) {
                    is ServerResult.Success -> {
                        _messages.postValue(result.data.messages!!)
                    }
                    is ServerResult.Failure -> {
                        _error.postValue(result.exception.message)
                    }
                    else -> {}
                }
            }
        }
    }

    fun sendMessage(request: SendMessageRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.sendMessage(request) { result ->
                when (result) {
                    is ServerResult.Success -> {
                        // Handle success if needed
                    }
                    is ServerResult.Failure -> {
                        _error.postValue(result.exception.message)
                    }
                    else -> {}
                }
            }
        }
    }

    fun sendMessageSocket(message: MessageEntity) {
        chatRepository.sendMessageSocket(message)
    }
}
