package com.example.mvvm_kotlin.api

import com.example.mvvm_kotlin.BuildConfig
import com.example.mvvm_kotlin.constant.Config
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private val retrofitInstances = HashMap<String, Retrofit>()

    /**
     * Replicates the dynamic base URL logic from your Mechanics project.
     * You can pass any URL from Config.kt here.
     */
    fun getInstance(baseURL: String): ApiService {
        if (!retrofitInstances.containsKey(baseURL)) {
            val builder = OkHttpClient.Builder()
                .connectTimeout(Config.TIMEOUT_MINUTES, TimeUnit.MINUTES)
                .readTimeout(Config.TIMEOUT_MINUTES, TimeUnit.MINUTES)
                .writeTimeout(Config.TIMEOUT_MINUTES, TimeUnit.MINUTES)

            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                builder.addInterceptor(logging)
            }

            val retrofit = Retrofit.Builder()
                .baseUrl(baseURL)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            
            retrofitInstances[baseURL] = retrofit
        }
        return retrofitInstances[baseURL]!!.create(ApiService::class.java)
    }
}
