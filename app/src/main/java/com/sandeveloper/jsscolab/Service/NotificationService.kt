package com.sandeveloper.jsscolab.Service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sandeveloper.jsscolab.domain.Constants.NotificationType
import com.sandeveloper.jsscolab.domain.HelperClasses.NotificationBuilderUtil.showNotification
import com.sandeveloper.jsscolab.domain.Constants.Endpoints.Notifications as N
import com.sandeveloper.jsscolab.domain.HelperClasses.PrefManager
import com.sandeveloper.jsscolab.domain.Models.FCMNotification
import timber.log.Timber

class NotificationService:FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.tag("FCMToken ").i("Refreshed token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        if (PrefManager.getUserFCMToken()!="" || PrefManager.getUserFCMToken()!!.isNotEmpty()){
            Timber.tag("SellerFirebaseService ").i("Message :: %s", message.data)

            val type = message.data[N.TYPE]
            val title = message.data[N.TITLE]
            val body = message.data[N.BODY]
            val address = message.data[N.ADDRESS]
            val from = message.data[N.fromUser]

            type?.let {
                if(type == NotificationType.CHAT_MESSAGE_NOTIFICATION.name){
                    val notif = FCMNotification(
                        notificationType = NotificationType.CHAT_MESSAGE_NOTIFICATION,
                        title = title!!,
                        desc = body!!,
                        message = from!!
                    )
                    showNotification(applicationContext,notif)

                }
                if(type == NotificationType.NEW_POST_NOTIFICATION.name){
                    val notif = FCMNotification(
                        notificationType = NotificationType.NEW_POST_NOTIFICATION,
                        title = title!!,
                        desc = body!!,
                        message = from!!,
                        address = address!!
                    )
                    showNotification(applicationContext,notif)
                }

            }

        }

    }
}