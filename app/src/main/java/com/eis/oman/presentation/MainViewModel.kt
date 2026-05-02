package com.eis.oman.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eis.oman.domain.model.AppLanguage
import com.eis.oman.domain.model.ThemeMode
import com.eis.oman.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class MainUiState(
    val useDarkTheme: Boolean = true,
    val language: AppLanguage = AppLanguage.ENGLISH,
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(MainUiState())
    val state: StateFlow<MainUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                settingsRepository.themeMode(),
                settingsRepository.language(),
            ) { theme, lang ->
                MainUiState(
                    useDarkTheme = when (theme) {
                        ThemeMode.DARK -> true
                        ThemeMode.LIGHT -> false
                        ThemeMode.SYSTEM -> true
                    },
                    language = lang,
                )
            }.collect { _state.value = it }
        }
    }
}
