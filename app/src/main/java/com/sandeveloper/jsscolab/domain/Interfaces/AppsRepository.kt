package com.sandeveloper.jsscolab.domain.Interfaces

import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Post.App
import com.sandeveloper.jsscolab.domain.Modules.app.AppEntity
import com.sandeveloper.jsscolab.domain.Modules.app.getAppResponse
import com.sandeveloper.jsscolab.domain.Modules.app.getCategoryResponse
import com.sandeveloper.jsscolab.domain.Modules.commonResponse
import com.sandeveloper.jsscolab.domain.Modules.swap.SwapEntity

interface AppsRepository {
    suspend fun getAppNames(serverResult: (ServerResult<getAppResponse>) -> Unit)
    suspend fun getAllApps(
        limit: Int?,
        category: String?,
        serverResult: (ServerResult<getAppResponse>) -> Unit
    )
    suspend fun getCategories(serverResult: (ServerResult<getCategoryResponse>) -> Unit)

    suspend fun setAppsInDB(apps:List<AppEntity>, serverResult: (ServerResult<commonResponse>) -> Unit)
    suspend fun getAppsNameFrom(id:String):List<App>
    suspend fun getCategoriesFromDb(): List<String>
}