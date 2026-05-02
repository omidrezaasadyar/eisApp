package com.eis.oman.domain.usecase

import com.eis.oman.domain.model.ServiceRequest
import com.eis.oman.domain.repository.ServiceRequestRepository
import javax.inject.Inject

class SubmitServiceRequestUseCase @Inject constructor(
    private val repository: ServiceRequestRepository,
) {
    suspend operator fun invoke(request: ServiceRequest): Result<String> {
        validate(request).onFailure { return Result.failure(it) }
        return repository.submit(request)
    }

    private fun validate(request: ServiceRequest): Result<Unit> {
        if (request.fullName.isBlank()) return Result.failure(ValidationError.FullNameRequired)
        if (request.email.isBlank() && request.phone.isBlank()) {
            return Result.failure(ValidationError.ContactRequired)
        }
        if (request.email.isNotBlank() && !request.email.contains("@")) {
            return Result.failure(ValidationError.InvalidEmail)
        }
        if (request.message.length < 10) return Result.failure(ValidationError.MessageTooShort)
        return Result.success(Unit)
    }
}

sealed class ValidationError(message: String) : Throwable(message) {
    data object FullNameRequired : ValidationError("full_name_required")
    data object ContactRequired : ValidationError("contact_required")
    data object InvalidEmail : ValidationError("invalid_email")
    data object MessageTooShort : ValidationError("message_too_short")
}
