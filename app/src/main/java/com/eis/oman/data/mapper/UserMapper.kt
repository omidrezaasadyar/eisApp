package com.eis.oman.data.mapper

import com.eis.oman.data.dto.UserDto
import com.eis.oman.domain.model.User

fun UserDto.toDomain(uid: String): User = User(
    uid = uid,
    email = email,
    firstName = firstName,
    lastName = lastName,
    phone = phone,
)

fun User.toDtoForCreate(locale: String): UserDto = UserDto(
    uid = uid,
    email = email,
    firstName = firstName,
    lastName = lastName,
    phone = phone,
    locale = locale,
)
