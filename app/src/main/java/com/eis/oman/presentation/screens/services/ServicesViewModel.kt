package com.eis.oman.presentation.screens.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eis.oman.domain.model.Service
import com.eis.oman.domain.usecase.GetServicesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface ServicesUiState {
    data object Loading : ServicesUiState
    data class Content(val services: List<Service>) : ServicesUiState
    data class Error(val messageKey: String) : ServicesUiState
}

@HiltViewModel
class ServicesViewModel @Inject constructor(
    private val getServices: GetServicesUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<ServicesUiState>(ServicesUiState.Loading)
    val state: StateFlow<ServicesUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            runCatching { getServices().collect { _state.value = ServicesUiState.Content(it) } }
                .onFailure { _state.value = ServicesUiState.Error(it.message.orEmpty()) }
        }
    }
}
