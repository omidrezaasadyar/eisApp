package com.eis.oman.presentation.screens.request

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.eis.oman.R
import com.eis.oman.presentation.components.PlaceholderScaffold

@Composable
fun RequestScreen(
    preselectedServiceKey: String?,
    onBack: () -> Unit,
    onSubmitted: () -> Unit,
) {
    PlaceholderScaffold(
        title = stringResource(R.string.nav_request),
        body = stringResource(R.string.request_placeholder),
        onBack = onBack,
    )
}
