package com.example.dayflow.di

import android.content.Context
import androidx.room.Room
import com.example.dayflow.data.local.LocalDataBase
import com.example.dayflow.data.repository.Repository
import com.example.dayflow.data.repository.RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    private const val LOCAL_DB_NAME = "tasksDataBase"

    @Provides
    @Singleton
    fun provideLocalDataBase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            LocalDataBase::class.java,
            LOCAL_DB_NAME
        ).build()
}