package com.eis.oman.di

import com.eis.oman.data.repository.ServiceRepositoryImpl
import com.eis.oman.data.repository.ServiceRequestRepositoryImpl
import com.eis.oman.data.repository.SettingsRepositoryImpl
import com.eis.oman.domain.repository.ServiceRepository
import com.eis.oman.domain.repository.ServiceRequestRepository
import com.eis.oman.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindServiceRepository(impl: ServiceRepositoryImpl): ServiceRepository

    @Binds
    @Singleton
    abstract fun bindServiceRequestRepository(impl: ServiceRequestRepositoryImpl): ServiceRequestRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository
}
