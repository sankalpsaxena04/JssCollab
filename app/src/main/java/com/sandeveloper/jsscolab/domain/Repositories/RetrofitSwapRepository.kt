package com.sandeveloper.jsscolab.domain.Repositories

import com.sandeveloper.jsscolab.data.Room.SwapDAO
import com.sandeveloper.jsscolab.domain.Api.SwapApi
import com.sandeveloper.jsscolab.domain.Interfaces.SwapRepository
import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.commonResponse
import com.sandeveloper.jsscolab.domain.Modules.swap.SwapEntity
import com.sandeveloper.jsscolab.domain.Modules.swap.SwapItem
import com.sandeveloper.jsscolab.domain.Modules.swap.createSwapRequest
import com.sandeveloper.jsscolab.domain.Modules.swap.getSwapResposne
import com.sandeveloper.jsscolab.domain.Modules.swap.getSwapsRequest
import com.sandeveloper.jsscolab.domain.Modules.swap.swapItemResponse
import com.sandeveloper.jsscolab.domain.Modules.swap.updateSwapRequest
import javax.inject.Inject

class RetrofitSwapRepository @Inject constructor(
    private val swapApi: SwapApi,
    private val swapItemDao: SwapDAO
):SwapRepository{
    // Swap posts
    override suspend fun createSwap(createSwapRequest: createSwapRequest, serverResult: (ServerResult<commonResponse>) -> Unit) {
        serverResult(ServerResult.Progress)
        try {
            val response = swapApi.createSwap(createSwapRequest)
            if (response.isSuccessful) {
                serverResult(ServerResult.Success(response.body()!!))
            } else {
                serverResult(ServerResult.Failure(Exception(response.message())))
            }
        } catch (e: Exception) {
            serverResult(ServerResult.Failure(e))
        }
    }

    override suspend fun getSwaps(getSwapsRequest: getSwapsRequest, serverResult: (ServerResult<getSwapResposne>) -> Unit) {
        serverResult(ServerResult.Progress)
        try {
            val response = swapApi.getSwaps(getSwapsRequest)
            if (response.isSuccessful) {
                serverResult(ServerResult.Success(response.body()!!))
            } else {
                serverResult(ServerResult.Failure(Exception(response.message())))
            }
        } catch (e: Exception) {
            serverResult(ServerResult.Failure(e))
        }
    }

    override suspend fun getMySwaps(serverResult: (ServerResult<getSwapResposne>) -> Unit) {
        serverResult(ServerResult.Progress)
        try {
            val response = swapApi.getMySwaps()
            if (response.isSuccessful) {
                serverResult(ServerResult.Success(response.body()!!))
            } else {
                serverResult(ServerResult.Failure(Exception(response.message())))
            }
        } catch (e: Exception) {
            serverResult(ServerResult.Failure(e))
        }
    }

    override suspend fun updateSwap(updateSwapRequest: updateSwapRequest, serverResult: (ServerResult<commonResponse>) -> Unit) {
        serverResult(ServerResult.Progress)
        try {
            val response = swapApi.updateSwap(updateSwapRequest)
            if (response.isSuccessful) {
                serverResult(ServerResult.Success(response.body()!!))
            } else {
                serverResult(ServerResult.Failure(Exception(response.message())))
            }
        } catch (e: Exception) {
            serverResult(ServerResult.Failure(e))
        }
    }

    override suspend fun deleteSwap(swapId: String, serverResult: (ServerResult<commonResponse>) -> Unit) {
        serverResult(ServerResult.Progress)
        try {
            val response = swapApi.deleteSwap(mapOf("swap_id" to swapId))
            if (response.isSuccessful) {
                serverResult(ServerResult.Success(response.body()!!))
            } else {
                serverResult(ServerResult.Failure(Exception(response.message())))
            }
        } catch (e: Exception) {
            serverResult(ServerResult.Failure(e))
        }
    }

    override suspend fun setSwapInDB(
        swaps: List<SwapEntity>,
        serverResult: (ServerResult<commonResponse>) -> Unit
    ) {
        serverResult(ServerResult.Progress)
        try {
            swapItemDao.insertSwaps(swaps)
            serverResult(ServerResult.Success(commonResponse(true, "Success",null)))
        }
        catch (e:Exception){
            serverResult(ServerResult.Failure(e))
        }
    }

    override suspend fun getSwapItems(serverResult: (ServerResult<swapItemResponse>) -> Unit) {
        serverResult(ServerResult.Progress)
        try{
            val response = swapApi.getSwapItems()
            if(response.isSuccessful){
                if(response.body()!!.success){
                    serverResult(ServerResult.Success(response.body()!!))

                }
                else{
                    serverResult(ServerResult.Failure(Exception(response.body()!!.message)))
                }
            }else{
                serverResult(ServerResult.Failure(Exception(response.message())))
            }
        }
        catch (e:Exception){
            serverResult(ServerResult.Failure(e))
        }
    }

    override suspend fun getSwapNameFrom(id: String): String {
        return swapItemDao.getNames(id)
    }


}