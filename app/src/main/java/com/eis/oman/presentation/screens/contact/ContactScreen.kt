package com.eis.oman.presentation.screens.contact

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.eis.oman.R
import com.eis.oman.presentation.components.PlaceholderScaffold

@Composable
fun ContactScreen(onBack: () -> Unit) {
    PlaceholderScaffold(
        title = stringResource(R.string.contact_title),
        body = stringResource(R.string.contact_body),
        onBack = onBack,
    )
}
