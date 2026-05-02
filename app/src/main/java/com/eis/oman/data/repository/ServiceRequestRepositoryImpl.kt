package com.eis.oman.data.repository

import com.eis.oman.core.DispatcherProvider
import com.eis.oman.data.mapper.toDto
import com.eis.oman.domain.model.ServiceRequest
import com.eis.oman.domain.repository.ServiceRequestRepository
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Singleton
class ServiceRequestRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val dispatchers: DispatcherProvider,
) : ServiceRequestRepository {

    override suspend fun submit(request: ServiceRequest): Result<String> =
        withContext(dispatchers.io) {
            runCatching {
                val ref = firestore.collection(COLLECTION).document()
                val dto = request.copy(id = ref.id).toDto()
                ref.set(dto).await()
                ref.id
            }
        }

    private companion object {
        const val COLLECTION = "service_requests"
    }
}
