package com.eis.oman.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eis.oman.domain.model.User
import com.eis.oman.domain.repository.AuthError
import com.eis.oman.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class LoginMode { SIGN_IN, SIGN_UP }

enum class LoginField { Username, Password, FirstName, LastName, Email, Phone }

enum class LoginErrorKind {
    Required,
    InvalidEmail,
    InvalidPhone,
    ShortPassword,
    NotEnglish,
}

enum class AuthBannerKind {
    InvalidCredentials,
    UserNotFound,
    EmailAlreadyInUse,
    WeakPassword,
    InvalidEmail,
    Network,
    Unknown,
}

data class LoginFormState(
    val mode: LoginMode = LoginMode.SIGN_IN,
    val username: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phone: String = "",
    val isSubmitting: Boolean = false,
    val fieldErrors: Map<LoginField, LoginErrorKind> = emptyMap(),
    val authError: AuthBannerKind? = null,
)

data class ForgotPasswordState(
    val visible: Boolean = false,
    val email: String = "",
    val isSending: Boolean = false,
    val emailError: LoginErrorKind? = null,
    val sentSuccessfully: Boolean = false,
    val authError: AuthBannerKind? = null,
)

data class LoginUiState(
    val signedInUser: User? = null,
    val form: LoginFormState = LoginFormState(),
    val forgotPassword: ForgotPasswordState = ForgotPasswordState(),
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val form = MutableStateFlow(LoginFormState())
    private val forgot = MutableStateFlow(ForgotPasswordState())

    val state: StateFlow<LoginUiState> = combine(
        authRepository.currentUser,
        form,
        forgot,
    ) { user, formState, forgotState ->
        LoginUiState(signedInUser = user, form = formState, forgotPassword = forgotState)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), LoginUiState())

    fun setMode(mode: LoginMode) = form.update {
        it.copy(mode = mode, fieldErrors = emptyMap(), authError = null)
    }

    fun togglePasswordVisible() = form.update { it.copy(passwordVisible = !it.passwordVisible) }

    fun onUsernameChange(v: String) = form.update {
        it.copy(username = v, fieldErrors = it.fieldErrors - LoginField.Username, authError = null)
    }
    fun onPasswordChange(v: String) = form.update {
        it.copy(password = v, fieldErrors = it.fieldErrors - LoginField.Password, authError = null)
    }
    fun onFirstNameChange(v: String) = form.update {
        it.copy(firstName = v, fieldErrors = it.fieldErrors - LoginField.FirstName, authError = null)
    }
    fun onLastNameChange(v: String) = form.update {
        it.copy(lastName = v, fieldErrors = it.fieldErrors - LoginField.LastName, authError = null)
    }
    fun onEmailChange(v: String) = form.update {
        it.copy(email = v, fieldErrors = it.fieldErrors - LoginField.Email, authError = null)
    }
    fun onPhoneChange(v: String) = form.update {
        it.copy(phone = v, fieldErrors = it.fieldErrors - LoginField.Phone, authError = null)
    }
    fun dismissAuthError() = form.update { it.copy(authError = null) }

    fun submit() {
        val current = form.value
        if (current.isSubmitting) return
        val errors = validate(current)
        if (errors.isNotEmpty()) {
            form.update { it.copy(fieldErrors = errors) }
            return
        }
        form.update { it.copy(isSubmitting = true, fieldErrors = emptyMap(), authError = null) }
        viewModelScope.launch {
            val result = if (current.mode == LoginMode.SIGN_IN) {
                authRepository.signIn(current.username, current.password)
            } else {
                authRepository.signUp(
                    email = current.email,
                    password = current.password,
                    firstName = current.firstName,
                    lastName = current.lastName,
                    phone = current.phone,
                )
            }
            result.onSuccess {
                form.update {
                    LoginFormState(mode = it.mode)
                }
            }.onFailure { error ->
                form.update { it.copy(isSubmitting = false, authError = error.toBanner()) }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch { authRepository.signOut() }
    }

    fun showForgotPassword() {
        // Pre-fill with whichever email-ish field the user has typed.
        val prefill = form.value.email.ifBlank { form.value.username }
        forgot.value = ForgotPasswordState(visible = true, email = prefill)
    }

    fun dismissForgotPassword() {
        forgot.value = ForgotPasswordState()
    }

    fun onForgotEmailChange(value: String) = forgot.update {
        it.copy(email = value, emailError = null, authError = null)
    }

    fun submitForgotPassword() {
        val current = forgot.value
        if (current.isSending) return
        val emailError = validateForgotEmail(current.email)
        if (emailError != null) {
            forgot.update { it.copy(emailError = emailError) }
            return
        }
        forgot.update { it.copy(isSending = true, emailError = null, authError = null) }
        viewModelScope.launch {
            authRepository.sendPasswordReset(current.email)
                .onSuccess {
                    forgot.update {
                        it.copy(isSending = false, sentSuccessfully = true)
                    }
                }
                .onFailure { error ->
                    forgot.update {
                        it.copy(isSending = false, authError = error.toBanner())
                    }
                }
        }
    }

    fun acknowledgeForgotPasswordSent() {
        forgot.value = ForgotPasswordState()
    }

    private fun validateForgotEmail(email: String): LoginErrorKind? = when {
        email.isBlank() -> LoginErrorKind.Required
        !ENGLISH_BASIC.matches(email) -> LoginErrorKind.NotEnglish
        !email.contains("@") || !email.contains(".") -> LoginErrorKind.InvalidEmail
        else -> null
    }

    private fun validate(state: LoginFormState): Map<LoginField, LoginErrorKind> {
        val errors = mutableMapOf<LoginField, LoginErrorKind>()
        if (state.mode == LoginMode.SIGN_IN) {
            if (state.username.isBlank()) errors[LoginField.Username] = LoginErrorKind.Required
            else if (!ENGLISH_BASIC.matches(state.username)) {
                errors[LoginField.Username] = LoginErrorKind.NotEnglish
            }
            if (state.password.isBlank()) errors[LoginField.Password] = LoginErrorKind.Required
            else if (state.password.length < 6) errors[LoginField.Password] = LoginErrorKind.ShortPassword
            else if (!ENGLISH_BASIC.matches(state.password)) {
                errors[LoginField.Password] = LoginErrorKind.NotEnglish
            }
        } else {
            errors.putIfMissing(state.firstName, LoginField.FirstName, ENGLISH_NAME)
            errors.putIfMissing(state.lastName, LoginField.LastName, ENGLISH_NAME)
            when {
                state.email.isBlank() -> errors[LoginField.Email] = LoginErrorKind.Required
                !ENGLISH_BASIC.matches(state.email) -> errors[LoginField.Email] = LoginErrorKind.NotEnglish
                !state.email.contains("@") || !state.email.contains(".") ->
                    errors[LoginField.Email] = LoginErrorKind.InvalidEmail
            }
            when {
                state.phone.isBlank() -> errors[LoginField.Phone] = LoginErrorKind.Required
                !ENGLISH_PHONE.matches(state.phone) -> errors[LoginField.Phone] = LoginErrorKind.NotEnglish
                state.phone.count { it.isDigit() } < 7 -> errors[LoginField.Phone] = LoginErrorKind.InvalidPhone
            }
            when {
                state.password.isBlank() -> errors[LoginField.Password] = LoginErrorKind.Required
                state.password.length < 6 -> errors[LoginField.Password] = LoginErrorKind.ShortPassword
                !ENGLISH_BASIC.matches(state.password) -> errors[LoginField.Password] = LoginErrorKind.NotEnglish
            }
        }
        return errors
    }

    private fun MutableMap<LoginField, LoginErrorKind>.putIfMissing(
        value: String,
        field: LoginField,
        regex: Regex,
    ) {
        when {
            value.isBlank() -> this[field] = LoginErrorKind.Required
            !regex.matches(value) -> this[field] = LoginErrorKind.NotEnglish
        }
    }

    private fun Throwable.toBanner(): AuthBannerKind = when (this) {
        AuthError.InvalidCredentials -> AuthBannerKind.InvalidCredentials
        AuthError.UserNotFound -> AuthBannerKind.UserNotFound
        AuthError.EmailAlreadyInUse -> AuthBannerKind.EmailAlreadyInUse
        AuthError.WeakPassword -> AuthBannerKind.WeakPassword
        AuthError.InvalidEmail -> AuthBannerKind.InvalidEmail
        AuthError.Network -> AuthBannerKind.Network
        else -> AuthBannerKind.Unknown
    }

    private companion object {
        // Letters, spaces, hyphens, apostrophes, periods (matches names like "O'Brien").
        val ENGLISH_NAME = Regex("^[A-Za-z][A-Za-z .'\\-]*$")
        // Standard ASCII characters that legitimately appear in emails / passwords.
        val ENGLISH_BASIC = Regex("^[\\u0020-\\u007E]+$")
        // Phone-friendly characters.
        val ENGLISH_PHONE = Regex("^[+\\-\\d\\s()]+$")
    }
}
