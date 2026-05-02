package com.eis.oman.domain.repository

import com.eis.oman.domain.model.Service
import com.eis.oman.domain.model.ServiceId
import kotlinx.coroutines.flow.Flow

interface ServiceRepository {
    fun observeServices(): Flow<List<Service>>
    suspend fun getService(id: ServiceId): Service?
}
