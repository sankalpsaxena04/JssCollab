package com.sandeveloper.jsscolab.domain.Repositories

import com.sandeveloper.jsscolab.domain.Api.CoshopApi
import com.sandeveloper.jsscolab.domain.Api.SwapApi
import com.sandeveloper.jsscolab.domain.Interfaces.PostRepository
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
import javax.inject.Inject

class RetrofitPostRepository @Inject constructor(
    private val coshopApi: CoshopApi,
    private val swapApi: SwapApi
) : PostRepository {

    // Co-shop posts
    override suspend fun createCoshopPost(createPost: createPost, serverResult: (ServerResult<commonResponse>) -> Unit) {
        serverResult(ServerResult.Progress)
        try {
            val response = coshopApi.createPost(createPost)
            if (response.isSuccessful) {
                serverResult(ServerResult.Success(response.body()!!))
            } else {
                serverResult(ServerResult.Failure(Exception(response.message())))
            }
        } catch (e: Exception) {
            serverResult(ServerResult.Failure(e))
        }
    }

    override suspend fun getCoshopPosts(getPostsRequest: getPostsRequest, serverResult: (ServerResult<getPostResponse>) -> Unit) {
        serverResult(ServerResult.Progress)
        try {
            val response = coshopApi.getPosts(getPostsRequest)
            if (response.isSuccessful) {
                serverResult(ServerResult.Success(response.body()!!))
            } else {
                serverResult(ServerResult.Failure(Exception(response.message())))
            }
        } catch (e: Exception) {
            serverResult(ServerResult.Failure(e))
        }
    }

    override suspend fun getMyCoshopPosts(serverResult: (ServerResult<getPostResponse>) -> Unit) {
        serverResult(ServerResult.Progress)
        try {
            val response = coshopApi.getMyPosts()
            if (response.isSuccessful) {
                serverResult(ServerResult.Success(response.body()!!))
            } else {
                serverResult(ServerResult.Failure(Exception(response.message())))
            }
        } catch (e: Exception) {
            serverResult(ServerResult.Failure(e))
        }
    }

    override suspend fun updateCoshopPost(updatePostRequest: updatePostRequest, serverResult: (ServerResult<commonResponse>) -> Unit) {
        serverResult(ServerResult.Progress)
        try {
            val response = coshopApi.updatePost(updatePostRequest)
            if (response.isSuccessful) {
                serverResult(ServerResult.Success(response.body()!!))
            } else {
                serverResult(ServerResult.Failure(Exception(response.message())))
            }
        } catch (e: Exception) {
            serverResult(ServerResult.Failure(e))
        }
    }

    override suspend fun deleteCoshopPost(postId: String, serverResult: (ServerResult<commonResponse>) -> Unit) {
        serverResult(ServerResult.Progress)
        try {
            val response = coshopApi.deletePost(mapOf("post_id" to postId))
            if (response.isSuccessful) {
                serverResult(ServerResult.Success(response.body()!!))
            } else {
                serverResult(ServerResult.Failure(Exception(response.message())))
            }
        } catch (e: Exception) {
            serverResult(ServerResult.Failure(e))
        }
    }

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
}
