package com.example.mvvm_kotlin.api

import com.example.mvvm_kotlin.model.Post
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("posts/1")
    suspend fun getSamplePost(): Response<Post>

    @POST("posts")
    suspend fun createPost(@Body post: Post): Response<Post>
}
