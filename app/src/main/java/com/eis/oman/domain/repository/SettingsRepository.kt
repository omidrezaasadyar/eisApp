package com.eis.oman.domain.repository

import com.eis.oman.domain.model.AppLanguage
import com.eis.oman.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun language(): Flow<AppLanguage>
    fun themeMode(): Flow<ThemeMode>
    suspend fun setLanguage(language: AppLanguage)
    suspend fun setThemeMode(mode: ThemeMode)
}
