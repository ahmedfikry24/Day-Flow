package com.example.dayflow.di

import android.content.Context
import androidx.room.Room
import com.example.dayflow.broadcasts.DefaultAlarmManager

import com.example.dayflow.data.local.LocalDataBase
import com.example.dayflow.data.local.data_store.DataStoreManager
import com.example.dayflow.notifications.DefaultNotificationManager
import com.example.dayflow.service.DefaultServiceManager
import com.example.dayflow.utils.DefaultDeviceInfoManager
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [LocalModule::class]
)
object FakeLocalModuleTest {

    @Provides
    @Singleton
    fun provideLocalDataBase(
        @ApplicationContext context: Context
    ) = Room.inMemoryDatabaseBuilder(context, LocalDataBase::class.java).build()

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
    fun provideDeviceInfoManager(
        @ApplicationContext context: Context
    ) = DefaultDeviceInfoManager(context)

    @Provides
    @Singleton
    fun provideDataStoreManager(
        @ApplicationContext context: Context,
    ) = DataStoreManager(context)
}