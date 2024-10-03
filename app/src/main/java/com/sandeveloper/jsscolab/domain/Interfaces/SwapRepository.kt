package com.sandeveloper.jsscolab.domain.Interfaces

import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.commonResponse
import com.sandeveloper.jsscolab.domain.Modules.swap.SwapEntity
import com.sandeveloper.jsscolab.domain.Modules.swap.SwapItem
import com.sandeveloper.jsscolab.domain.Modules.swap.createSwapRequest
import com.sandeveloper.jsscolab.domain.Modules.swap.getSwapResposne
import com.sandeveloper.jsscolab.domain.Modules.swap.getSwapsRequest
import com.sandeveloper.jsscolab.domain.Modules.swap.swapItemResponse
import com.sandeveloper.jsscolab.domain.Modules.swap.updateSwapRequest

interface SwapRepository {

    suspend fun getSwapItems(serverResult: (ServerResult<swapItemResponse>) -> Unit)
    suspend fun createSwap(createSwapRequest: createSwapRequest, serverResult: (ServerResult<commonResponse>) -> Unit)
    suspend fun getSwaps(getSwapsRequest: getSwapsRequest, serverResult: (ServerResult<getSwapResposne>) -> Unit)
    suspend fun getMySwaps(serverResult: (ServerResult<getSwapResposne>) -> Unit)
    suspend fun updateSwap(updateSwapRequest: updateSwapRequest, serverResult: (ServerResult<commonResponse>) -> Unit)
    suspend fun deleteSwap(swapId: String, serverResult: (ServerResult<commonResponse>) -> Unit)
    suspend fun setSwapInDB(swaps:List<SwapEntity>, serverResult: (ServerResult<commonResponse>) -> Unit)
    suspend fun getSwapNameFrom(id:String):String

}