package com.eis.oman.core

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.eis.oman.domain.model.AppLanguage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocaleManager @Inject constructor() {

    fun apply(language: AppLanguage) {
        val tag = when (language) {
            AppLanguage.ENGLISH -> "en"
            AppLanguage.ARABIC -> "ar"
        }
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(tag))
    }

    fun current(): AppLanguage {
        val locales = AppCompatDelegate.getApplicationLocales()
        val first = locales[0]?.language
        return if (first == "ar") AppLanguage.ARABIC else AppLanguage.ENGLISH
    }
}
