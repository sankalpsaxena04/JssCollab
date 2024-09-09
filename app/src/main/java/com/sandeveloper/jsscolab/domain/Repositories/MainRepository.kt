package com.sandeveloper.jsscolab.domain.Repositories

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.sandeveloper.jsscolab.domain.Api.appsApi
import com.sandeveloper.jsscolab.domain.HelperClasses.PrefManager
import com.sandeveloper.jsscolab.domain.Interfaces.AppsRepository
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.app.getAppResponse
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(  private val api: appsApi) :AppsRepository {

    private suspend fun <T> handleApiResponse(
        apiCall: suspend () -> Response<T>,
        serverResult: (ServerResult<T>) -> Unit,
        onSuccess: (T) -> Unit = {}
    ) {
        try {
            serverResult(ServerResult.Progress)
            val response = apiCall()
            if (response.isSuccessful) {
                response.body()?.let {
                    serverResult(ServerResult.Success(it))
                } ?: serverResult(ServerResult.Failure(Exception("Response body is null")))
            } else {
                serverResult(ServerResult.Failure(Exception("Failed with status code: ${response.code()}")))
            }
        } catch (e: Exception) {
            serverResult(ServerResult.Failure(e))
        }
    }

    override suspend fun getNames(serverResult: (ServerResult<getAppResponse>) -> Unit) {
        handleApiResponse(
            apiCall = { api.getNames() },
            serverResult = serverResult,
            onSuccess = {
                if(it.success) {
                    val groupedApps = it.apps!!.groupBy { it.category }
                     val resultList = mutableListOf<Map<String, List<String>>>()
                    for ((category, appList) in groupedApps) {
                        val appNames = appList.map { it.name }
                        resultList.add(mapOf(category to appNames))
                    }
                    PrefManager.setAppList(resultList)


                }
            }
        )
    }

    override suspend fun getAllApps(
        limit: Int?,
        category: String?,
        serverResult: (ServerResult<getAppResponse>) -> Unit
    ) {
        handleApiResponse(
            apiCall = { api.getAllApps(limit, category) },
            serverResult = serverResult
        )
    }
}
