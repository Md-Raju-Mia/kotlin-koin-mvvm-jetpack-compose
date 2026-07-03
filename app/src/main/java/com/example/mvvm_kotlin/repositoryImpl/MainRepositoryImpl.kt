package com.example.mvvm_kotlin.repositoryImpl

import android.content.Context
import com.example.mvvm_kotlin.api.ApiService
import com.example.mvvm_kotlin.data.local.PostDao
import com.example.mvvm_kotlin.data.local.PostEntity
import com.example.mvvm_kotlin.model.Post
import com.example.mvvm_kotlin.repository.MainRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Response
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val postDao: PostDao,
    private val apiService: ApiService
) : MainRepository {
    override suspend fun getSamplePost(): Response<Post> {
        return apiService.getSamplePost()
    }

    override suspend fun createPost(post: Post): Response<Post> {
        return apiService.createPost(post)
    }

    override suspend fun savePostLocal(post: PostEntity) {
        postDao.insertPost(post)
    }

    override suspend fun getPostsLocal(): List<PostEntity> {
        return postDao.getAllPosts()
    }
}
