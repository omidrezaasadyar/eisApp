package com.eis.oman.data.dto

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class UserDto(
    @DocumentId val uid: String? = null,
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val phone: String = "",
    val locale: String = "",
    @ServerTimestamp val createdAt: Date? = null,
    @ServerTimestamp val updatedAt: Date? = null,
)
