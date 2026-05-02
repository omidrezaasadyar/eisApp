package com.eis.oman.domain.usecase

import com.eis.oman.domain.model.Service
import com.eis.oman.domain.repository.ServiceRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetServicesUseCase @Inject constructor(
    private val repository: ServiceRepository,
) {
    operator fun invoke(): Flow<List<Service>> = repository.observeServices()
}
