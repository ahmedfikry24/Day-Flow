package com.example.dayflow.di

import android.content.Context
import androidx.room.Room
import com.example.dayflow.data.local.LocalDataBase
import com.example.dayflow.data.local.data_store.DataStoreManager
import com.example.dayflow.data.repository.Repository
import com.example.dayflow.data.usecase.AddDailyTaskUseCase
import com.example.dayflow.data.usecase.DeleteDailyTaskUseCase
import com.example.dayflow.data.usecase.UpdateDailyTaskStatusUseCase
import com.example.dayflow.data.utils.PackageAppsManager
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

    @Provides
    @Singleton
    fun provideDeleteDailyTaskUseCase(
        repository: Repository,
        @ApplicationContext context: Context,
    ) = DeleteDailyTaskUseCase(repository, context)


    @Provides
    @Singleton
    fun provideAddDailyTaskUseCase(
        repository: Repository,
        @ApplicationContext context: Context,
    ) = AddDailyTaskUseCase(repository, context)

    @Provides
    @Singleton
    fun provideUpdateDailyTaskStatusUseCase(
        repository: Repository,
        @ApplicationContext context: Context,
    ) = UpdateDailyTaskStatusUseCase(repository, context)


    @Provides
    @Singleton
    fun provideDataStoreManager(
        @ApplicationContext context: Context,
    ) = DataStoreManager(context)

    @Provides
    @Singleton
    fun providePackageAppsManager(
        @ApplicationContext context: Context
    ) = PackageAppsManager(context)
}