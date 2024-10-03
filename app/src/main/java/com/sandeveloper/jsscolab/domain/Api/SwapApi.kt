package com.sandeveloper.jsscolab.domain.Api

import com.sandeveloper.jsscolab.domain.Modules.commonResponse
import com.sandeveloper.jsscolab.domain.Modules.swap.createSwapRequest
import com.sandeveloper.jsscolab.domain.Modules.swap.getSwapResposne
import com.sandeveloper.jsscolab.domain.Modules.swap.getSwapsRequest
import com.sandeveloper.jsscolab.domain.Modules.swap.swapItemResponse
import com.sandeveloper.jsscolab.domain.Modules.swap.updateSwapRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
interface SwapApi {
    @POST("swap/create-swap")
    suspend fun createSwap(@Body createSwap: createSwapRequest): Response<commonResponse>

    @GET("swap/get-swap-items")
    suspend fun getSwapItems(): Response<swapItemResponse>

    @POST("swap/get-swaps")
    suspend fun getSwaps(@Body getSwapsRequest: getSwapsRequest): Response<getSwapResposne>

    @GET("swap/get-my-swaps")
    suspend fun getMySwaps(): Response<getSwapResposne>

    @PATCH("swap/update-swap")
    suspend fun updateSwap(@Body updateSwapRequest: updateSwapRequest): Response<commonResponse>

    @DELETE("swap/delete-swap")
    suspend fun deleteSwap(@Body swap_id: Map<String,String>): Response<commonResponse>
    // mapOf("swap_id" to swap_id)
}