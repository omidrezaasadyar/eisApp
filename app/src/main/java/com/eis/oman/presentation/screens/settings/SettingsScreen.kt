package com.eis.oman.presentation.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.eis.oman.R
import com.eis.oman.presentation.components.PlaceholderScaffold

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    PlaceholderScaffold(
        title = stringResource(R.string.nav_settings),
        body = stringResource(R.string.settings_placeholder),
        onBack = onBack,
    )
}
