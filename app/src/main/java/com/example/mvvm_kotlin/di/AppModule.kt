package com.example.mvvm_kotlin.di

import androidx.room.Room
import com.example.mvvm_kotlin.data.local.AppDatabase
import com.example.mvvm_kotlin.repository.MainRepository
import com.example.mvvm_kotlin.repositoryImpl.MainRepositoryImpl
import com.example.mvvm_kotlin.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Room Database
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java, "app_database"
        ).build()
    }

    // Dao
    single { get<AppDatabase>().postDao() }

    // Repository
    single<MainRepository> { MainRepositoryImpl(androidContext(), get()) }
    
    // ViewModel
    viewModel { MainViewModel(get()) }
}
