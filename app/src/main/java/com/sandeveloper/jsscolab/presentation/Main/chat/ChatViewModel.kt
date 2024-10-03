package com.sandeveloper.jsscolab.presentation.Main.chat

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Messages.Message
import com.sandeveloper.jsscolab.domain.Modules.Messages.MessageEntity
import com.sandeveloper.jsscolab.domain.Modules.Messages.RoomIdResponse
import com.sandeveloper.jsscolab.domain.Modules.Messages.SendMessageRequest
import com.sandeveloper.jsscolab.domain.Repositories.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URISyntaxException
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> = _messages

    private val _newMessage = MutableLiveData<Message>()
    val newMessage: LiveData<Message> = _newMessage

    private val _roomId = MutableLiveData<ServerResult<RoomIdResponse>>()
    val roomId: LiveData<ServerResult<RoomIdResponse>> = _roomId

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _socketError = MutableLiveData<String>()
    val socketError: LiveData<String> get() = _socketError

    private val _cameOnlineCount = MutableLiveData<Int>().apply { postValue(0) }
    val cameOnlineCount: LiveData<Int> = _cameOnlineCount

    private val _isUserOnline = MutableLiveData<Boolean>()
    val isUserOnline: LiveData<Boolean> = _isUserOnline

    private val _receivedMessage = MutableLiveData<Message>()
    val receivedMessage: LiveData<Message> get() = _receivedMessage

    private lateinit var socket: Socket

    init {
        initializeSocket()
        listen()
    }

    fun initializeSocket() {
        try {
            socket = IO.socket("BASE_URL")
            socket.connect()
        } catch (e: Exception) {
            Log.d("SOCKET", e.message.toString())
        }
    }

    fun listen() {
        socket.on("error") { args ->
            val errorMessage = args[0].toString()
            Log.d("SOCKET", errorMessage)
            _socketError.postValue(errorMessage)
        }

        socket.on("came-online") { args ->
            Log.d("SOCKET", "came-online: ${args[0]}")
            _cameOnlineCount.postValue(_cameOnlineCount.value?.plus(1))
        }

        socket.on("came-offline") { args ->
            Log.d("SOCKET", "came-offline: ${args[0]}")
            _cameOnlineCount.postValue(_cameOnlineCount.value?.minus(1))
        }

        socket.on("is-user-online") { args ->
            if (args.isNotEmpty()) {
                val room = args[0] as String
                val isOnline = args[1] as Boolean
                Log.d("SOCKET", "Room: $room, Is Online: $isOnline")
                _isUserOnline.postValue(isOnline)
            }
        }

        socket.on("receive-message") { args ->
            if (args.isNotEmpty()) {
                val room = args[0] as String
                val data = args[1] as JSONObject

                val sender = data.getString("sender")
                val text = data.getString("text")
                val timeSent = data.getLong("timeSent")

                val message = Message(sender, text, timeSent)
                _receivedMessage.postValue(message)

                Log.d("SocketIO", "Message received in room $room: $message")
            }
        }
    }

    fun disconnectSocket() {
        socket.disconnect()
    }

    fun joinRoom(roomId: String, token: String) {
        val req = JSONObject().apply {
            put("room", roomId)
            put("token", token)
        }
        socket.emit("join-room", req)
    }

    fun leaveRoom(roomId: String) {
        socket.emit("leave-room", roomId)
    }

    fun notifyOnline(roomId: String) {
        socket.emit("notify-online", roomId)
    }

    fun getStatus(roomId: String) {
        socket.emit("get-status", roomId)
    }

    fun sendMessage(message: Message) {
        val req = JSONObject()
        req.put("room", "correct-room-id") // Use actual room ID, not sender
        val messageJson = JSONObject().apply {
            put("sender", message.sender)
            put("text", message.text)
            put("timeSent", message.timeSent)
        }
        req.put("data", messageJson)

        socket.emit("send-message", req)
    }

    fun fetchRoomId(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.getRoomId(userId) { result ->

                _roomId.postValue(result)
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
}
