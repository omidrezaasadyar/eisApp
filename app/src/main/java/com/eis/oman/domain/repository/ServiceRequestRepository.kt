package com.eis.oman.domain.repository

import com.eis.oman.domain.model.ServiceRequest

interface ServiceRequestRepository {
    suspend fun submit(request: ServiceRequest): Result<String>
}
