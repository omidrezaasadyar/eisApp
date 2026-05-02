package com.eis.oman.data.dto

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class ServiceRequestDto(
    @DocumentId val id: String? = null,
    val serviceId: String = "",
    val fullName: String = "",
    val company: String = "",
    val email: String = "",
    val phone: String = "",
    val message: String = "",
    val locale: String = "",
    val status: String = "new",
    @ServerTimestamp val createdAt: Date? = null,
    @ServerTimestamp val updatedAt: Date? = null,
)
