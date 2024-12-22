package com.sandeveloper.jsscolab.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sandeveloper.jsscolab.data.Room.Message
import com.sandeveloper.jsscolab.data.Room.People
import com.sandeveloper.jsscolab.domain.Interfaces.AppsRepository
import com.sandeveloper.jsscolab.domain.Interfaces.MessageRepository
import com.sandeveloper.jsscolab.domain.Interfaces.ProfileRepository
import com.sandeveloper.jsscolab.domain.Interfaces.SwapRepository
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Profile.BanStatusResponse
import com.sandeveloper.jsscolab.domain.Modules.Profile.MyProfileResponse
import com.sandeveloper.jsscolab.domain.Modules.app.AppEntity
import com.sandeveloper.jsscolab.domain.Modules.app.getAppResponse
import com.sandeveloper.jsscolab.domain.Modules.commonResponse
import com.sandeveloper.jsscolab.domain.Modules.swap.SwapEntity
import com.sandeveloper.jsscolab.domain.Modules.swap.swapItemResponse
import com.sandeveloper.jsscolab.domain.Repositories.RetrofitAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartScreenViewModel @Inject constructor(
   private val authRepository: RetrofitAuthRepository,
   private val swapRepository: SwapRepository,
   private val profileRepository: ProfileRepository,
   private val appsRepository: AppsRepository,
   private val messaageRepository: MessageRepository,
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
    private val _setFCMtokenResponse = MutableLiveData<ServerResult<commonResponse>>()
    val setFCMtokenResponse: LiveData<ServerResult<commonResponse>> = _setFCMtokenResponse
    private val _getAppAppsResponse = MutableLiveData<ServerResult<getAppResponse>>()
    val getAppAppsResponse: LiveData<ServerResult<getAppResponse>> = _getAppAppsResponse

    private val _messageResponse = MutableLiveData<ServerResult<Boolean>>()
    val messageResponse: LiveData<ServerResult<Boolean>> = _messageResponse

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

    fun setFCMToken(token:String){
        viewModelScope.launch {
            profileRepository.setFcm(mapOf("FCM_token" to token)){
                _setFCMtokenResponse.postValue(it)
            }
        }
    }
    fun getApps(limit:Int?,category:String?) {
        viewModelScope.launch {
            appsRepository.getAllApps(limit, category) { result ->
                _getAppAppsResponse.postValue(result)
            }
        }
    }
    fun setAppsToDb(apps:List<AppEntity>){
        viewModelScope.launch {
            appsRepository.setAppsInDB(apps){result->
                _swapItemsDBResponse.postValue(result)
            }
        }
    }


    fun getMessages() {
        val tag = "GetMessagesFlow"

        viewModelScope.launch {
            Log.d(tag, "Checking for messages")

            messaageRepository.checkForMessages() {result->
                when (result){
                is ServerResult.Failure -> {
                    Log.e(tag, "Failed to check for messages: ${result.exception.message}")
                    _messageResponse.postValue(ServerResult.Failure(result.exception))
                }
                ServerResult.Progress -> {
                    Log.d(tag, "Message checking in progress")
                }
                is ServerResult.Success -> {
                    Log.d(tag, "Successfully checked messages. Success: ${result.data.success}, New messages found: ${result.data.newMessagesFound}")

                    if (result.data.success && result.data.newMessagesFound) {
                        result.data.messagesCount?.forEach { room ->
                            Log.d(tag, "Processing room: ${room._id}")

                            CoroutineScope(Dispatchers.IO).launch {
                                processRoomMessages(room._id)
                            }
                        }
                    } else {
                        Log.d(tag, "No new messages found")
                        _messageResponse.postValue(ServerResult.Success(true))
                    }
                }
            }
        }
    }
    }

    private suspend fun processRoomMessages(roomId: String) {
        val tag = "ProcessRoomMessages"

        if (!messaageRepository.roomExistsDB(roomId)) {
            Log.d(tag, "Room does not exist in DB. Fetching user profile for room: $roomId")

            messaageRepository.getUserProfile(roomId, null){profileResult->
                when (profileResult){
                is ServerResult.Success -> {
                    val person = profileResult.data.room.user
                    Log.d(tag, "User profile fetched for room: $roomId, User: ${person.full_name}")

                    CoroutineScope(Dispatchers.IO).launch {
                        insertPersonAndMessages(People(person._id,roomId,person.full_name,person.photo?:"",false), roomId)
                    }
                }
                is ServerResult.Failure -> {
                    Log.e(tag, "Failed to fetch user profile for room: $roomId, Error: ${profileResult.exception.message}")
                }
                else -> Unit
            }
            }
        } else {
            Log.d(tag, "Room already exists in DB. Fetching messages for room: $roomId")
            fetchAndInsertMessages(roomId)
        }
    }

    private suspend fun insertPersonAndMessages(person: People, roomId: String) {
        val tag = "InsertPersonAndMessages"

        if (messaageRepository.insertPersonDB(
                person
            ) == ServerResult.Success(Unit)
        ) {
            Log.d(tag, "Person inserted into DB: ${person.senderName}, Room: $roomId")
            fetchAndInsertMessages(roomId)
        } else {
            Log.e(tag, "Failed to insert person into DB for room: $roomId")
        }
    }

    private suspend fun fetchAndInsertMessages(roomId: String) {
        val tag = "FetchAndInsertMessages"

         messaageRepository.getMessages(roomId) {messageResult->
             when (messageResult){
            is ServerResult.Success -> {
                messageResult.data.messages?.forEach { message ->
                    CoroutineScope(Dispatchers.IO).launch {
                        if (messaageRepository.insertMessageDB(
                                Message(
                                    roomId = roomId,
                                    message = message.text,
                                    timeSent = message.timeSent.time,
                                    isSender = false,
                                    isRead = false
                                )
                            ) == ServerResult.Success(Unit)
                        ) {
                            deleteMessages(roomId)
                            Log.d(tag, "Message inserted for room: $roomId, Message: ${message.text}")
                        } else {
                            Log.e(tag, "Failed to insert message for room: $roomId")
                        }
                    }
                }
                _messageResponse.postValue(ServerResult.Success(true))
            }
            is ServerResult.Failure -> {
                Log.e(tag, "Failed to fetch messages for room: $roomId, Error: ${messageResult.exception.message}")
            }
            else -> Unit
        }
         }
    }


    fun deleteMessages(roomId:String){
        viewModelScope.launch {
            messaageRepository.deleteMessages(roomId){
                if(it is ServerResult.Success){
                    Log.d("GetMessagesFlow","Messages Deleted")
                }
            }
        }
    }

}