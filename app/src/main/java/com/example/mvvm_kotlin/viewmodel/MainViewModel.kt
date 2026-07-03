package com.example.mvvm_kotlin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm_kotlin.data.local.PostEntity
import com.example.mvvm_kotlin.model.Post
import com.example.mvvm_kotlin.repository.MainRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    private val _post = MutableLiveData<Post?>()
    val post: LiveData<Post?> get() = _post

    private val _postResponse = MutableLiveData<String>()
    val postResponse: LiveData<String> get() = _postResponse

    private val _localPosts = MutableLiveData<List<PostEntity>>()
    val localPosts: LiveData<List<PostEntity>> get() = _localPosts

    fun fetchPost() {
        viewModelScope.launch {
            try {
                val response = repository.getSamplePost()
                if (response.isSuccessful) {
                    val fetchedPost = response.body()
                    _post.postValue(fetchedPost)
                    
                    // Automatically save to Room for demonstration
                    fetchedPost?.let {
                        repository.savePostLocal(PostEntity(it.id, it.userId, it.title, it.body))
                        loadLocalPosts()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadLocalPosts() {
        viewModelScope.launch {
            val posts = repository.getPostsLocal()
            _localPosts.postValue(posts)
        }
    }

    fun createPost(title: String, body: String) {
        viewModelScope.launch {
            try {
                val newPost = Post(userId = 1, id = 0, title = title, body = body)
                val response = repository.createPost(newPost)
                if (response.isSuccessful) {
                    _postResponse.postValue("Success: Post ID ${response.body()?.id} created")
                } else {
                    _postResponse.postValue("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _postResponse.postValue("Exception: ${e.message}")
            }
        }
    }
}
