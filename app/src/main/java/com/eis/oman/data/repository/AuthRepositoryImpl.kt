package com.eis.oman.data.repository

import com.eis.oman.core.DispatcherProvider
import com.eis.oman.core.LocaleManager
import com.eis.oman.data.dto.UserDto
import com.eis.oman.data.mapper.toDomain
import com.eis.oman.data.mapper.toDtoForCreate
import com.eis.oman.domain.model.User
import com.eis.oman.domain.repository.AuthError
import com.eis.oman.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val localeManager: LocaleManager,
    private val dispatchers: DispatcherProvider,
) : AuthRepository {

    /**
     * Emits the signed-in user enriched with the Firestore profile, or null
     * when signed out. Re-fetches the profile every time the auth state
     * changes so first/last name appear immediately after sign-up.
     */
    override val currentUser: Flow<User?> = uidFlow()
        .flatMapLatest { uid ->
            if (uid == null) flowOf<User?>(null)
            else flow<User?> {
                emit(fetchProfile(uid) ?: minimalUser(uid))
            }
        }
        .flowOn(dispatchers.io)

    override suspend fun signIn(email: String, password: String): Result<User> =
        withContext(dispatchers.io) {
            runCatching {
                val result = auth.signInWithEmailAndPassword(email.trim(), password).await()
                val uid = result.user?.uid ?: error("auth.user.uid was null")
                fetchProfile(uid) ?: minimalUser(uid)
            }.recoverCatching { throw it.toAuthError() }
        }

    override suspend fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        phone: String,
    ): Result<User> = withContext(dispatchers.io) {
        runCatching {
            val sanitisedEmail = email.trim()
            val result = auth.createUserWithEmailAndPassword(sanitisedEmail, password).await()
            val uid = result.user?.uid ?: error("auth.user.uid was null")
            val user = User(
                uid = uid,
                email = sanitisedEmail,
                firstName = firstName.trim(),
                lastName = lastName.trim(),
                phone = phone.trim(),
            )
            firestore.collection(USERS).document(uid)
                .set(user.toDtoForCreate(locale = localeManager.current().tag))
                .await()
            user
        }.recoverCatching { throw it.toAuthError() }
    }

    override suspend fun signOut() = withContext(dispatchers.io) {
        auth.signOut()
    }

    private fun uidFlow(): Flow<String?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebase ->
            trySend(firebase.currentUser?.uid)
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    private suspend fun fetchProfile(uid: String): User? {
        val doc = firestore.collection(USERS).document(uid).get().await()
        val dto = doc.toObject(UserDto::class.java) ?: return null
        return dto.toDomain(uid)
    }

    private fun minimalUser(uid: String): User {
        val firebaseUser = auth.currentUser
        return User(
            uid = uid,
            email = firebaseUser?.email.orEmpty(),
            firstName = "",
            lastName = "",
            phone = firebaseUser?.phoneNumber.orEmpty(),
        )
    }

    private fun Throwable.toAuthError(): AuthError = when (this) {
        is FirebaseAuthInvalidCredentialsException -> {
            if (errorCode == "ERROR_INVALID_EMAIL") AuthError.InvalidEmail
            else AuthError.InvalidCredentials
        }
        is FirebaseAuthInvalidUserException -> AuthError.UserNotFound
        is FirebaseAuthUserCollisionException -> AuthError.EmailAlreadyInUse
        is FirebaseAuthWeakPasswordException -> AuthError.WeakPassword
        is IOException -> AuthError.Network
        else -> AuthError.Unknown
    }

    private companion object {
        const val USERS = "users"
    }
}
