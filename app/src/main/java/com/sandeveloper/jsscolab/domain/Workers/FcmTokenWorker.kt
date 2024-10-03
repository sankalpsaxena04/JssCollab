package com.sandeveloper.jsscolab.domain.Workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sandeveloper.jsscolab.domain.Interfaces.ProfileRepository
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import timber.log.Timber

class FcmTokenWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val profileRepository: ProfileRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val fcmToken = inputData.getString("fcmToken") ?: return Result.failure()
        return try {

            var response:Result = Result.failure()
            fcmToken.let {token->
                profileRepository.setFcm(mapOf("FCM_token" to token)){result->
                    when(result){
                        is ServerResult.Failure -> {
                            Timber.tag("WorkManager")
                                .e("Failed to update token: " + result.exception.message)
                            response =Result.failure()
                        }
                        ServerResult.Progress -> {
                            response = Result.retry()
                        }
                        is ServerResult.Success ->{
                            Timber.tag("WorkManager")
                                .d("Token updated successfully: " + result.data.message)
                            response = Result.success()
                        }
                    }

                }
                if (response==Result.success()) {
                    Log.d("WorkManager", "Token updated successfully")
                    Result.success()
                } else {
                    Log.e("WorkManager", "Failed to update token")
                    Result.failure()
                }
            }
        } catch (e: Exception) {
            Log.e("WorkManager", "Error updating token: ${e.message}")
            Result.failure()
        }

    }
}
