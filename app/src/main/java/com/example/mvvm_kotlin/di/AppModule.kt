package com.example.mvvm_kotlin.di

import android.content.Context
import androidx.room.Room
import com.example.mvvm_kotlin.BuildConfig
import com.example.mvvm_kotlin.api.ApiService
import com.example.mvvm_kotlin.constant.Config
import com.example.mvvm_kotlin.data.local.AppDatabase
import com.example.mvvm_kotlin.data.local.PostDao
import com.example.mvvm_kotlin.repository.MainRepository
import com.example.mvvm_kotlin.repositoryImpl.MainRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(Config.TIMEOUT_MINUTES, TimeUnit.MINUTES)
            .readTimeout(Config.TIMEOUT_MINUTES, TimeUnit.MINUTES)
            .writeTimeout(Config.TIMEOUT_MINUTES, TimeUnit.MINUTES)

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(logging)
        }
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideApiService(okHttpClient: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl(Config.BASE_URL_POSTS)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "app_database"
        ).build()
    }

    @Provides
    fun providePostDao(appDatabase: AppDatabase): PostDao {
        return appDatabase.postDao()
    }

    @Provides
    @Singleton
    fun provideMainRepository(
        @ApplicationContext context: Context,
        postDao: PostDao,
        apiService: ApiService
    ): MainRepository {
        return MainRepositoryImpl(context, postDao, apiService)
    }
}
