package com.example.dayflow.di

import android.content.Context
import androidx.room.Room
import com.example.dayflow.data.alarm.DefaultAlarmManager
import com.example.dayflow.data.local.LocalDataBase
import com.example.dayflow.data.local.data_store.DataStoreManager
import com.example.dayflow.data.utils.DefaultNotificationManager
import com.example.dayflow.data.utils.PackageAppsManager
import com.example.dayflow.service.DefaultServiceManager
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
    fun provideDefaultNotificationManager(
        @ApplicationContext context: Context
    ) = DefaultNotificationManager(context)


    @Provides
    @Singleton
    fun provideDefaultAlarmManager(
        @ApplicationContext context: Context
    ) = DefaultAlarmManager(context)

    @Provides
    @Singleton
    fun provideDefaultServiceManager(
        @ApplicationContext context: Context
    ) = DefaultServiceManager(context)


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