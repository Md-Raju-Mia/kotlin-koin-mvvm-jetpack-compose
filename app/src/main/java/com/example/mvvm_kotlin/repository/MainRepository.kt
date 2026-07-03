package com.example.mvvm_kotlin.repository

import com.example.mvvm_kotlin.data.local.PostEntity
import com.example.mvvm_kotlin.model.Post
import retrofit2.Response

interface MainRepository {
    suspend fun getSamplePost(): Response<Post>
    suspend fun createPost(post: Post): Response<Post>

    // Room operations
    suspend fun savePostLocal(post: PostEntity)
    suspend fun getPostsLocal(): List<PostEntity>
}
