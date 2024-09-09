package com.sandeveloper.jsscolab.domain.Interfaces

import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.app.getAppResponse

interface AppsRepository {
    suspend fun getNames(serverResult: (ServerResult<getAppResponse>) -> Unit)
    suspend fun getAllApps(
        limit: Int?,
        category: String?,
        serverResult: (ServerResult<getAppResponse>) -> Unit
    )
}