package com.eis.oman.presentation.screens.contact

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eis.oman.R
import com.eis.oman.presentation.components.ContactRow

@Composable
fun ContactScreen(
    onBack: () -> Unit,
    viewModel: ContactViewModel = hiltViewModel(),
) {
    val snackbarHost = remember { SnackbarHostState() }
    val noHandlerMessage = stringResource(R.string.error_no_handler)
    val whatsAppGreeting = stringResource(R.string.whatsapp_prefilled_general)
    val emailSubject = stringResource(R.string.brand_full_name)

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            if (effect == ContactEffect.NoHandler) {
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
                        text = stringResource(R.string.contact_title),
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(R.string.contact_body),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            ContactRow(
                label = stringResource(R.string.action_whatsapp),
                value = viewModel.contactInfo.whatsappE164,
                icon = ImageVector.vectorResource(R.drawable.ic_whatsapp),
                onClick = { viewModel.onWhatsAppClick(whatsAppGreeting) },
            )
            ContactRow(
                label = stringResource(R.string.contact_phone_label),
                value = viewModel.contactInfo.phoneE164,
                icon = Icons.Outlined.Call,
                onClick = viewModel::onCallClick,
            )
            ContactRow(
                label = stringResource(R.string.contact_email_label),
                value = viewModel.contactInfo.email,
                icon = Icons.Outlined.Email,
                onClick = { viewModel.onEmailClick(emailSubject) },
            )
            ContactRow(
                label = stringResource(R.string.contact_website_label),
                value = stringResource(R.string.eis_website_display),
                icon = Icons.Outlined.Language,
                onClick = viewModel::onWebsiteClick,
            )
            ContactRow(
                label = stringResource(R.string.contact_address_label),
                value = stringResource(R.string.contact_address_value),
                icon = Icons.Outlined.LocationOn,
                onClick = { /* no-op until a maps URL is configured */ },
            )
        }
    }
}
