package com.eis.oman.data.repository

import com.eis.oman.domain.model.Service
import com.eis.oman.domain.model.ServiceId
import com.eis.oman.domain.repository.ServiceRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * The catalogue of services is fixed and ships with the app — there is no
 * remote source. Localized copy is resolved in the presentation layer via
 * string resources keyed by [ServiceId.key].
 */
@Singleton
class ServiceRepositoryImpl @Inject constructor() : ServiceRepository {

    private val services: List<Service> = ServiceId.entries.mapIndexed { index, id ->
        Service(id = id, order = index)
    }

    override fun observeServices(): Flow<List<Service>> = flowOf(services)

    override suspend fun getService(id: ServiceId): Service? =
        services.firstOrNull { it.id == id }
}
