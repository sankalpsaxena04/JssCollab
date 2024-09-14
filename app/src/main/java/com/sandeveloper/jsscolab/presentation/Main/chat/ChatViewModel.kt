package com.sandeveloper.jsscolab.presentation.Main.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Messages.GetMessagesResponse
import com.sandeveloper.jsscolab.domain.Modules.Messages.Message
import com.sandeveloper.jsscolab.domain.Modules.Messages.SendMessageRequest
import com.sandeveloper.jsscolab.domain.Repositories.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    val messagesLiveData = MutableLiveData<ServerResult<GetMessagesResponse>>()
    val newMessageLiveData = MutableLiveData<ServerResult<Message>>()

    init {
        repository.initializeSocket()
    }

    fun connectToSocket() {
        repository.connectSocket()
        repository.listenForMessages { newMessage ->
            newMessageLiveData.postValue(newMessage)
        }
    }

    fun disconnectFromSocket() {
        repository.disconnectSocket()
    }

    fun fetchMessages(roomId: String) {
        viewModelScope.launch {
            messagesLiveData.postValue(ServerResult.Progress) // show progress
            repository.getMessages(roomId){result->

                messagesLiveData.postValue(result)
            }
        }
    }

    fun sendMessage(request: SendMessageRequest) {
        viewModelScope.launch {
            repository.sendMessage(request){
                TODO("handle this")
            }
        }
    }

    fun sendMessageViaSocket(message: Message) {
        repository.sendMessageSocket(message)
    }
}
