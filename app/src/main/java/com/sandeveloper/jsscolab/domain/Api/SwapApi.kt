package com.sandeveloper.jsscolab.domain.Api

import com.sandeveloper.jsscolab.domain.Modules.commonResponse
import com.sandeveloper.jsscolab.domain.Modules.swap.createSwapRequest
import com.sandeveloper.jsscolab.domain.Modules.swap.getSwapResposne
import com.sandeveloper.jsscolab.domain.Modules.swap.getSwapsRequest
import com.sandeveloper.jsscolab.domain.Modules.swap.updateSwapRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
interface SwapApi {
    @POST("/create-swap")
    suspend fun createSwap(@Body createSwap: createSwapRequest): Response<commonResponse>


    @POST("/get-swaps")
    suspend fun getSwaps(@Body getSwapsRequest: getSwapsRequest): Response<getSwapResposne>

    @GET("/get-my-swaps")
    suspend fun getMySwaps(): Response<getSwapResposne>

    @PATCH("/update-swap")
    suspend fun updateSwap(@Body updateSwapRequest: updateSwapRequest): Response<commonResponse>

    @DELETE("/delete-swap")
    suspend fun deleteSwap(@Body swap_id: Map<String,String>): Response<commonResponse>
    // mapOf("swap_id" to swap_id)
}