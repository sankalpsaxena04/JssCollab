package com.sandeveloper.jsscolab.domain.Api

import com.sandeveloper.jsscolab.domain.Modules.Post.createPost
import com.sandeveloper.jsscolab.domain.Modules.Post.getPostResponse
import com.sandeveloper.jsscolab.domain.Modules.Post.getPostsRequest
import com.sandeveloper.jsscolab.domain.Modules.Post.updatePostRequest
import com.sandeveloper.jsscolab.domain.Modules.commonResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface CoshopApi {
    @POST("/create-post")
    suspend fun createPost(@Body createPost: createPost): Response<commonResponse>

    @POST("/get-posts")
    suspend fun getPosts(@Body getPosts: getPostsRequest): Response<getPostResponse>

    @GET("/get-my-posts")
    suspend fun getMyPosts(): Response<getPostResponse>

    @PATCH("/update-post")
    suspend fun updatePost(@Body updatePost: updatePostRequest): Response<commonResponse>

    @DELETE("/delete-post")
    suspend fun deletePost(@Body postId: Map<String,String>): Response<commonResponse>
    //mapOf("post_id" to postId)
}