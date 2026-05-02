package com.eis.oman.presentation.screens.settings

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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SettingsUiState(
    val language: AppLanguage = AppLanguage.ENGLISH,
    val themeMode: ThemeMode = ThemeMode.DARK,
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val localeManager: LocaleManager,
) : ViewModel() {

    val state: StateFlow<SettingsUiState> = combine(
        settingsRepository.language(),
        settingsRepository.themeMode(),
    ) { lang, theme -> SettingsUiState(language = lang, themeMode = theme) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SettingsUiState())

    fun selectLanguage(language: AppLanguage) {
        viewModelScope.launch {
            settingsRepository.setLanguage(language)
            localeManager.apply(language)
        }
    }

    fun selectTheme(mode: ThemeMode) {
        viewModelScope.launch { settingsRepository.setThemeMode(mode) }
    }
}
