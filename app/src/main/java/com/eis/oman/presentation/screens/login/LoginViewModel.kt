package com.eis.oman.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class LoginMode { SIGN_IN, SIGN_UP }

enum class LoginField { Username, Password, FirstName, LastName, Email, Phone }

enum class LoginErrorKind { Required, InvalidEmail, InvalidPhone, ShortPassword }

data class LoginUiState(
    val mode: LoginMode = LoginMode.SIGN_IN,
    // Sign-in fields
    val username: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    // Sign-up fields
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phone: String = "",
    // Form state
    val isSubmitting: Boolean = false,
    val isSubmitted: Boolean = false,
    val errors: Map<LoginField, LoginErrorKind> = emptyMap(),
)

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    fun setMode(mode: LoginMode) = _state.update {
        it.copy(mode = mode, errors = emptyMap(), isSubmitted = false)
    }

    fun togglePasswordVisible() = _state.update { it.copy(passwordVisible = !it.passwordVisible) }

    fun onUsernameChange(v: String) = _state.update { it.copy(username = v, errors = it.errors - LoginField.Username) }
    fun onPasswordChange(v: String) = _state.update { it.copy(password = v, errors = it.errors - LoginField.Password) }
    fun onFirstNameChange(v: String) = _state.update { it.copy(firstName = v, errors = it.errors - LoginField.FirstName) }
    fun onLastNameChange(v: String) = _state.update { it.copy(lastName = v, errors = it.errors - LoginField.LastName) }
    fun onEmailChange(v: String) = _state.update { it.copy(email = v, errors = it.errors - LoginField.Email) }
    fun onPhoneChange(v: String) = _state.update { it.copy(phone = v, errors = it.errors - LoginField.Phone) }

    fun submit() {
        val current = _state.value
        if (current.isSubmitting) return
        val errors = validate(current)
        if (errors.isNotEmpty()) {
            _state.update { it.copy(errors = errors) }
            return
        }
        _state.update { it.copy(isSubmitting = true, errors = emptyMap()) }
        viewModelScope.launch {
            // Backend wiring is pending; simulate a brief network call so the
            // user sees a clear "submitting" state.
            delay(900)
            _state.update { it.copy(isSubmitting = false, isSubmitted = true) }
        }
    }

    fun resetSubmittedFlag() = _state.update { it.copy(isSubmitted = false) }

    private fun validate(state: LoginUiState): Map<LoginField, LoginErrorKind> {
        val errors = mutableMapOf<LoginField, LoginErrorKind>()
        if (state.mode == LoginMode.SIGN_IN) {
            if (state.username.isBlank()) errors[LoginField.Username] = LoginErrorKind.Required
            if (state.password.isBlank()) errors[LoginField.Password] = LoginErrorKind.Required
            else if (state.password.length < 6) errors[LoginField.Password] = LoginErrorKind.ShortPassword
        } else {
            if (state.firstName.isBlank()) errors[LoginField.FirstName] = LoginErrorKind.Required
            if (state.lastName.isBlank()) errors[LoginField.LastName] = LoginErrorKind.Required
            if (state.email.isBlank()) errors[LoginField.Email] = LoginErrorKind.Required
            else if (!state.email.contains("@") || !state.email.contains(".")) {
                errors[LoginField.Email] = LoginErrorKind.InvalidEmail
            }
            if (state.phone.isBlank()) errors[LoginField.Phone] = LoginErrorKind.Required
            else if (!state.phone.any { it.isDigit() } || state.phone.count { it.isDigit() } < 7) {
                errors[LoginField.Phone] = LoginErrorKind.InvalidPhone
            }
            if (state.password.isBlank()) errors[LoginField.Password] = LoginErrorKind.Required
            else if (state.password.length < 6) errors[LoginField.Password] = LoginErrorKind.ShortPassword
        }
        return errors
    }
}
