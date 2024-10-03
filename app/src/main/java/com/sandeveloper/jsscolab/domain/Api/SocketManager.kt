package com.sandeveloper.jsscolab.domain.Api

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.sandeveloper.jsscolab.BuildConfig
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.commonResponse
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import java.net.URISyntaxException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocketManager @Inject constructor() {

    private val _joinRoomResponse = MutableLiveData<ServerResult<commonResponse>>()
    val joinRoomResponse: MutableLiveData<ServerResult<commonResponse>> = _joinRoomResponse
    private lateinit var socket: Socket

    fun initializeSocket(token: String) {
        try {

            // Point this URL to your Socket.IO server endpoint
            socket = IO.socket(BuildConfig.BASE_URL)
            socket.connect()


        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    fun joinRoom(roomId: String,token:String) {
        socket.on("join-room"){

        }
    }
    fun leaveRoom(roomId: String){
        socket.emit("leave-room", roomId)
    }
    fun notifyOnline(roomId: String){
        socket.emit("notify-online", roomId)
    }
    fun getStatus(roomId: String){
        socket.emit("get-status", roomId)
    }


    fun sendMessage(roomId: String, message: String) {
        val data = JSONObject()
        data.put("room", roomId)
        data.put("message", message)

        socket.emit("send-message", data)
    }

    fun listenForMessages(listener: (String) -> Unit) {
        socket.on("receive-message") { args ->
            if (args.isNotEmpty()) {
                val message = args[0] as String
                listener(message)
            }
        }
    }

    fun disconnect() {
        socket.disconnect()
    }
}
