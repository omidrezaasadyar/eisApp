package com.eis.oman.presentation.screens.service_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Send
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eis.oman.R
import com.eis.oman.domain.model.ServiceId
import com.eis.oman.presentation.screens.services.descRes
import com.eis.oman.presentation.screens.services.icon
import com.eis.oman.presentation.screens.services.titleRes

@Composable
fun ServiceDetailScreen(
    serviceKey: String,
    onBack: () -> Unit,
    onRequest: () -> Unit,
    viewModel: ServiceDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val snackbarHost = remember { SnackbarHostState() }
    val noHandlerMessage = stringResource(R.string.error_no_handler)

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            if (effect == ServiceDetailEffect.NoHandler) {
                snackbarHost.showSnackbar(noHandlerMessage)
            }
        }
    }

    Scaffold(
        containerColor = androidx.compose.ui.graphics.Color.Transparent,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.service_detail_title),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.action_back),
                        )
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHost) },
    ) { padding ->
        when (val s = state) {
            ServiceDetailUiState.NotFound -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.error_generic),
                    color = MaterialTheme.colorScheme.error,
                )
            }
            is ServiceDetailUiState.Content -> ServiceDetailContent(
                id = s.id,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                onRequest = onRequest,
                onWhatsApp = viewModel::onWhatsAppClick,
                onCall = viewModel::onCallClick,
            )
        }
    }
}

@Composable
private fun ServiceDetailContent(
    id: ServiceId,
    modifier: Modifier,
    onRequest: () -> Unit,
    onWhatsApp: (String) -> Unit,
    onCall: () -> Unit,
) {
    val titleText = stringResource(id.titleRes())
    val whatsappText = stringResource(R.string.whatsapp_prefilled_message, titleText)

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier.size(72.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = id.icon(),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                }
            }
            Text(
                text = titleText,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        Text(
            text = stringResource(id.descRes()),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Button(onClick = onRequest, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Outlined.Send, contentDescription = null)
                Text(
                    text = stringResource(R.string.nav_request),
                    modifier = Modifier.padding(start = 8.dp),
                )
            }
            OutlinedButton(
                onClick = { onWhatsApp(whatsappText) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_whatsapp),
                    contentDescription = null,
                )
                Text(
                    text = stringResource(R.string.action_whatsapp),
                    modifier = Modifier.padding(start = 8.dp),
                )
            }
            OutlinedButton(onClick = onCall, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Outlined.Call, contentDescription = null)
                Text(
                    text = stringResource(R.string.action_call),
                    modifier = Modifier.padding(start = 8.dp),
                )
            }
        }
    }
}
