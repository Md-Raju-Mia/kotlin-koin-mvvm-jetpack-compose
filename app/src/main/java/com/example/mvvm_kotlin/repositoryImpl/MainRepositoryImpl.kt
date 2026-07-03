package com.example.mvvm_kotlin.repositoryImpl

import android.content.Context
import com.example.mvvm_kotlin.api.RetrofitClient
import com.example.mvvm_kotlin.constant.Config
import com.example.mvvm_kotlin.data.local.PostDao
import com.example.mvvm_kotlin.data.local.PostEntity
import com.example.mvvm_kotlin.model.Post
import com.example.mvvm_kotlin.repository.MainRepository
import retrofit2.Response

class MainRepositoryImpl(
    private val context: Context,
    private val postDao: PostDao
) : MainRepository {
    override suspend fun getSamplePost(): Response<Post> {
        // Now fetching the API instance using the URL from Config, 
        // matching the Mechanics project's dynamic URL handling.
        return RetrofitClient.getInstance(Config.BASE_URL_POSTS).getSamplePost()
    }

    override suspend fun createPost(post: Post): Response<Post> {
        return RetrofitClient.getInstance(Config.BASE_URL_POSTS).createPost(post)
    }

    override suspend fun savePostLocal(post: PostEntity) {
        postDao.insertPost(post)
    }

    override suspend fun getPostsLocal(): List<PostEntity> {
        return postDao.getAllPosts()
    }
}
