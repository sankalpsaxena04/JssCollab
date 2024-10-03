package com.sandeveloper.jsscolab.domain.Workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
//import com.sandeveloper.jsscolab.domain.HelperClasses.NotificationBuilderUtil
//import com.sandeveloper.jsscolab.domain.Models.FCMNotification
import com.sandeveloper.jsscolab.domain.Repositories.NotificationRepository
import java.lang.Exception


class NotificationWorker(
    private val context: Context,
    params: WorkerParameters,
    private val notificationRepository: NotificationRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val token = inputData.getString("fcmToken") ?: return Result.failure()
        val topic = inputData.getString("topic") ?: return Result.failure()

        return try {
            val response = notificationRepository.subscribeToTopic(token, topic)
            if (response.success) Result.success() else Result.failure()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
