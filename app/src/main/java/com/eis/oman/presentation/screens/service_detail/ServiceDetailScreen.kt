package com.eis.oman.presentation.screens.service_detail

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.eis.oman.R
import com.eis.oman.presentation.components.PlaceholderScaffold

@Composable
fun ServiceDetailScreen(
    serviceKey: String,
    onBack: () -> Unit,
    onRequest: () -> Unit,
) {
    PlaceholderScaffold(
        title = stringResource(R.string.service_detail_title),
        body = stringResource(R.string.service_detail_placeholder, serviceKey),
        onBack = onBack,
    ) {
        Button(onClick = onRequest, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.nav_request))
        }
    }
}
