package com.eis.oman.presentation.screens.about

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.eis.oman.R
import com.eis.oman.presentation.components.PlaceholderScaffold

@Composable
fun AboutScreen(onBack: () -> Unit) {
    PlaceholderScaffold(
        title = stringResource(R.string.about_title),
        body = stringResource(R.string.about_body),
        onBack = onBack,
    )
}
