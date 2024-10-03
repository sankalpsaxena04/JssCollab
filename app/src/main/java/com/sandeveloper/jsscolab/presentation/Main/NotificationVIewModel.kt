package com.sandeveloper.jsscolab.presentation.Main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sandeveloper.jsscolab.domain.Modules.Notification.ChatNotificationRequest
import com.sandeveloper.jsscolab.domain.Modules.Notification.NotificationResponse
import com.sandeveloper.jsscolab.domain.Repositories.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _notificationResponse = MutableLiveData<NotificationResponse>()
    val notificationResponse: LiveData<NotificationResponse> get() = _notificationResponse

    fun subscribeToTopic(token: String, topic: String) = viewModelScope.launch {
        val response = notificationRepository.subscribeToTopic(token, topic)
        _notificationResponse.postValue(response)
    }

    fun sendChatNotification(token: String, title: String, body: String) = viewModelScope.launch {
        val response = notificationRepository.sendChatNotification(token, title, body)
        _notificationResponse.postValue(response)
    }

    fun sendPostNotification(topic: String, title: String, body: String) = viewModelScope.launch {
        val response = notificationRepository.sendPostNotification(topic, title, body)
        _notificationResponse.postValue(response)
    }
}
