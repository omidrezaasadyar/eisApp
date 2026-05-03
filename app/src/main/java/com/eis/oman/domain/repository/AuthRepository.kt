package com.eis.oman.domain.repository

import com.eis.oman.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    /** Emits the currently authenticated user, or null if signed out. */
    val currentUser: Flow<User?>

    suspend fun signIn(email: String, password: String): Result<User>

    suspend fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        phone: String,
    ): Result<User>

    suspend fun signOut()

    /** Sends a password-reset email to [email]. */
    suspend fun sendPasswordReset(email: String): Result<Unit>
}

/**
 * Stable error categories surfaced from the auth backend so the
 * presentation layer can show localised messages without coupling
 * to FirebaseAuth exception types.
 */
sealed class AuthError(message: String) : Throwable(message) {
    data object InvalidCredentials : AuthError("invalid_credentials")
    data object UserNotFound : AuthError("user_not_found")
    data object EmailAlreadyInUse : AuthError("email_already_in_use")
    data object WeakPassword : AuthError("weak_password")
    data object InvalidEmail : AuthError("invalid_email")
    data object Network : AuthError("network")
    data object Unknown : AuthError("unknown")
}
