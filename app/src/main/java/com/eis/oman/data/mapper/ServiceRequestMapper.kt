package com.eis.oman.data.mapper

import com.eis.oman.data.dto.ServiceRequestDto
import com.eis.oman.domain.model.ServiceId
import com.eis.oman.domain.model.ServiceRequest
import com.eis.oman.domain.model.ServiceRequestStatus

fun ServiceRequest.toDto(): ServiceRequestDto = ServiceRequestDto(
    id = id,
    serviceId = serviceId.key,
    fullName = fullName,
    company = company,
    email = email,
    phone = phone,
    message = message,
    locale = locale,
    status = status.key,
)

fun ServiceRequestDto.toDomain(): ServiceRequest? {
    val service = ServiceId.fromKey(serviceId) ?: return null
    return ServiceRequest(
        id = id,
        serviceId = service,
        fullName = fullName,
        company = company,
        email = email,
        phone = phone,
        message = message,
        locale = locale,
        status = ServiceRequestStatus.fromKey(status),
    )
}
