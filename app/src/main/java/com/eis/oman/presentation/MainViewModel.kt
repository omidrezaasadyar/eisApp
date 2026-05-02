package com.eis.oman.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eis.oman.core.LocaleManager
import com.eis.oman.domain.model.AppLanguage
import com.eis.oman.domain.model.ThemeMode
import com.eis.oman.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

data class MainUiState(
    val themeMode: ThemeMode = ThemeMode.DARK,
    val language: AppLanguage = AppLanguage.ENGLISH,
)

@HiltViewModel
class MainViewModel @Inject constructor(
    settingsRepository: SettingsRepository,
    localeManager: LocaleManager,
) : ViewModel() {

    val state: StateFlow<MainUiState> = combine(
        settingsRepository.themeMode(),
        settingsRepository.language(),
    ) { theme, lang -> MainUiState(themeMode = theme, language = lang) }
        .onEach { localeManager.apply(it.language) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), MainUiState())
}
