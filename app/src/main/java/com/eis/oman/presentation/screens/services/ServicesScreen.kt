package com.eis.oman.presentation.screens.services

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.eis.oman.R
import com.eis.oman.domain.model.ServiceId
import com.eis.oman.presentation.components.PlaceholderScaffold

@Composable
fun ServicesScreen(
    onBack: () -> Unit,
    onOpenService: (ServiceId) -> Unit,
) {
    PlaceholderScaffold(
        title = stringResource(R.string.nav_services),
        body = stringResource(R.string.services_placeholder),
        onBack = onBack,
    )
}
