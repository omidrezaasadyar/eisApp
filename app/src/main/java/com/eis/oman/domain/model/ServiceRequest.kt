package com.eis.oman.domain.model

data class ServiceRequest(
    val id: String? = null,
    val serviceId: ServiceId,
    val fullName: String,
    val company: String,
    val email: String,
    val phone: String,
    val message: String,
    val locale: String,
    val status: ServiceRequestStatus = ServiceRequestStatus.NEW,
)

enum class ServiceRequestStatus(val key: String) {
    NEW("new"),
    IN_REVIEW("in_review"),
    CONTACTED("contacted"),
    CLOSED("closed");

    companion object {
        fun fromKey(key: String?): ServiceRequestStatus =
            entries.firstOrNull { it.key == key } ?: NEW
    }
}
