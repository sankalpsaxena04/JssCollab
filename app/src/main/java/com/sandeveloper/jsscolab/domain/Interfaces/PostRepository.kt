package com.sandeveloper.jsscolab.domain.Interfaces

import com.sandeveloper.jsscolab.domain.Models.ServerResult
import com.sandeveloper.jsscolab.domain.Modules.Post.createPost
import com.sandeveloper.jsscolab.domain.Modules.Post.getPostResponse
import com.sandeveloper.jsscolab.domain.Modules.Post.getPostsRequest
import com.sandeveloper.jsscolab.domain.Modules.Post.updatePostRequest
import com.sandeveloper.jsscolab.domain.Modules.commonResponse
import com.sandeveloper.jsscolab.domain.Modules.swap.createSwapRequest
import com.sandeveloper.jsscolab.domain.Modules.swap.getSwapResposne
import com.sandeveloper.jsscolab.domain.Modules.swap.getSwapsRequest
import com.sandeveloper.jsscolab.domain.Modules.swap.updateSwapRequest
import retrofit2.Response

interface PostRepository {

    // Co-shop posts
    suspend fun createCoshopPost(createPost: createPost, serverResult: (ServerResult<commonResponse>) -> Unit)
    suspend fun getCoshopPosts(getPostsRequest: getPostsRequest, serverResult: (ServerResult<getPostResponse>) -> Unit)
    suspend fun getMyCoshopPosts(serverResult: (ServerResult<getPostResponse>) -> Unit)
    suspend fun updateCoshopPost(updatePostRequest: updatePostRequest, serverResult: (ServerResult<commonResponse>) -> Unit)
    suspend fun deleteCoshopPost(postId: String, serverResult: (ServerResult<commonResponse>) -> Unit)

    // Swap posts
    suspend fun createSwap(createSwapRequest: createSwapRequest, serverResult: (ServerResult<commonResponse>) -> Unit)
    suspend fun getSwaps(getSwapsRequest: getSwapsRequest, serverResult: (ServerResult<getSwapResposne>) -> Unit)
    suspend fun getMySwaps(serverResult: (ServerResult<getSwapResposne>) -> Unit)
    suspend fun updateSwap(updateSwapRequest: updateSwapRequest, serverResult: (ServerResult<commonResponse>) -> Unit)
    suspend fun deleteSwap(swapId: String, serverResult: (ServerResult<commonResponse>) -> Unit)
}
