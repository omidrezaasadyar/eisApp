package com.eis.oman.presentation.screens.request

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eis.oman.core.LocaleManager
import com.eis.oman.domain.model.ServiceId
import com.eis.oman.domain.model.ServiceRequest
import com.eis.oman.domain.usecase.SubmitServiceRequestUseCase
import com.eis.oman.domain.usecase.ValidationError
import com.eis.oman.presentation.navigation.EISRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class RequestField { FullName, Company, Email, Phone, Message }

data class RequestFormState(
    val serviceId: ServiceId = ServiceId.INDUSTRIAL_SOFTWARE,
    val fullName: String = "",
    val company: String = "",
    val email: String = "",
    val phone: String = "",
    val message: String = "",
    val isSubmitting: Boolean = false,
    val isSubmitted: Boolean = false,
    val errorField: RequestField? = null,
    val genericErrorKey: String? = null,
)

@HiltViewModel
class RequestViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val submitRequest: SubmitServiceRequestUseCase,
    private val localeManager: LocaleManager,
) : ViewModel() {

    private val _state = MutableStateFlow(
        RequestFormState(
            serviceId = savedStateHandle.get<String>(EISRoute.Request.ARG_SERVICE_ID)
                ?.let { ServiceId.fromKey(it) }
                ?: ServiceId.INDUSTRIAL_SOFTWARE,
        )
    )
    val state: StateFlow<RequestFormState> = _state.asStateFlow()

    fun onServiceSelected(id: ServiceId) = _state.update { it.copy(serviceId = id) }
    fun onFullNameChange(v: String) = _state.update { it.copy(fullName = v, errorField = null) }
    fun onCompanyChange(v: String) = _state.update { it.copy(company = v) }
    fun onEmailChange(v: String) = _state.update { it.copy(email = v, errorField = null) }
    fun onPhoneChange(v: String) = _state.update { it.copy(phone = v, errorField = null) }
    fun onMessageChange(v: String) = _state.update { it.copy(message = v, errorField = null) }
    fun dismissError() = _state.update { it.copy(genericErrorKey = null) }

    fun submit() {
        val current = _state.value
        if (current.isSubmitting) return
        _state.update { it.copy(isSubmitting = true, errorField = null, genericErrorKey = null) }

        viewModelScope.launch {
            val request = ServiceRequest(
                serviceId = current.serviceId,
                fullName = current.fullName.trim(),
                company = current.company.trim(),
                email = current.email.trim(),
                phone = current.phone.trim(),
                message = current.message.trim(),
                locale = localeManager.current().tag,
            )
            submitRequest(request)
                .onSuccess {
                    _state.update {
                        it.copy(isSubmitting = false, isSubmitted = true)
                    }
                }
                .onFailure { err ->
                    val (field, key) = mapError(err)
                    _state.update {
                        it.copy(isSubmitting = false, errorField = field, genericErrorKey = key)
                    }
                }
        }
    }

    private fun mapError(error: Throwable): Pair<RequestField?, String?> = when (error) {
        ValidationError.FullNameRequired -> RequestField.FullName to "full_name_required"
        ValidationError.ContactRequired -> RequestField.Email to "contact_required"
        ValidationError.InvalidEmail -> RequestField.Email to "invalid_email"
        ValidationError.MessageTooShort -> RequestField.Message to "message_too_short"
        else -> null to "generic"
    }
}
