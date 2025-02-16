package com.example.dayflow.di

import com.example.dayflow.data.repository.FakeRepositoryImpl
import com.example.dayflow.data.repository.Repository
import com.example.dayflow.data.repository.RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
abstract class FakeRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepository(fakeRepositoryImpl: FakeRepositoryImpl): Repository
}

