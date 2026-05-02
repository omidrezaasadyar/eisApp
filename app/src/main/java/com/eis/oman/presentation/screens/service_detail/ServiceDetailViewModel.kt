package com.eis.oman.presentation.screens.service_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.eis.oman.core.IntentLauncher
import com.eis.oman.core.LaunchOutcome
import com.eis.oman.domain.model.ContactInfo
import com.eis.oman.domain.model.ServiceId
import com.eis.oman.presentation.navigation.EISRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

sealed interface ServiceDetailUiState {
    data class Content(val id: ServiceId) : ServiceDetailUiState
    data object NotFound : ServiceDetailUiState
}

sealed interface ServiceDetailEffect {
    data object NoHandler : ServiceDetailEffect
}

@HiltViewModel
class ServiceDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val contactInfo: ContactInfo,
    private val intentLauncher: IntentLauncher,
) : ViewModel() {

    private val _state: MutableStateFlow<ServiceDetailUiState> = MutableStateFlow(
        run {
            val key = savedStateHandle.get<String>(EISRoute.ServiceDetail.ARG_SERVICE_ID).orEmpty()
            val id = ServiceId.fromKey(key)
            if (id == null) ServiceDetailUiState.NotFound
            else ServiceDetailUiState.Content(id)
        }
    )
    val state: StateFlow<ServiceDetailUiState> = _state.asStateFlow()

    private val _effects = Channel<ServiceDetailEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onWhatsAppClick(prefilledMessage: String) {
        report(intentLauncher.openWhatsApp(contactInfo.whatsappE164, prefilledMessage))
    }

    fun onCallClick() {
        report(intentLauncher.dial(contactInfo.phoneE164))
    }

    private fun report(outcome: LaunchOutcome) {
        if (outcome is LaunchOutcome.NoHandler) _effects.trySend(ServiceDetailEffect.NoHandler)
    }
}
