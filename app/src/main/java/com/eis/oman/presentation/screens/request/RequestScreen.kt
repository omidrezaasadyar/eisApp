package com.eis.oman.presentation.screens.request

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eis.oman.R
import com.eis.oman.domain.model.ServiceId
import com.eis.oman.presentation.screens.services.titleRes

@Composable
fun RequestScreen(
    preselectedServiceKey: String?,
    onBack: () -> Unit,
    onSubmitted: () -> Unit,
    viewModel: RequestViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isSubmitted) {
        if (state.isSubmitted) onSubmitted()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.nav_request),
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
        }
    ) { padding ->
        if (state.isSubmitting) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
            }
        } else if (state.isSubmitted) {
            SuccessView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                onClose = onSubmitted,
            )
        } else {
            FormView(
                state = state,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                onServiceSelected = viewModel::onServiceSelected,
                onFullNameChange = viewModel::onFullNameChange,
                onCompanyChange = viewModel::onCompanyChange,
                onEmailChange = viewModel::onEmailChange,
                onPhoneChange = viewModel::onPhoneChange,
                onMessageChange = viewModel::onMessageChange,
                onSubmit = viewModel::submit,
            )
        }
    }
}

@Composable
private fun FormView(
    state: RequestFormState,
    modifier: Modifier,
    onServiceSelected: (ServiceId) -> Unit,
    onFullNameChange: (String) -> Unit,
    onCompanyChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onMessageChange: (String) -> Unit,
    onSubmit: () -> Unit,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ServiceDropdown(
            selected = state.serviceId,
            onSelected = onServiceSelected,
        )

        OutlinedTextField(
            value = state.fullName,
            onValueChange = onFullNameChange,
            label = { Text(stringResource(R.string.request_full_name)) },
            isError = state.errorField == RequestField.FullName,
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            supportingText = errorTextFor(state, RequestField.FullName),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = state.company,
            onValueChange = onCompanyChange,
            label = { Text(stringResource(R.string.request_company)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = state.email,
            onValueChange = onEmailChange,
            label = { Text(stringResource(R.string.request_email)) },
            isError = state.errorField == RequestField.Email,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
            ),
            supportingText = errorTextFor(state, RequestField.Email),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = state.phone,
            onValueChange = onPhoneChange,
            label = { Text(stringResource(R.string.request_phone)) },
            isError = state.errorField == RequestField.Phone,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next,
            ),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = state.message,
            onValueChange = onMessageChange,
            label = { Text(stringResource(R.string.request_message)) },
            isError = state.errorField == RequestField.Message,
            minLines = 4,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
            supportingText = errorTextFor(state, RequestField.Message),
            modifier = Modifier.fillMaxWidth(),
        )

        if (state.genericErrorKey == "generic") {
            Text(
                text = stringResource(R.string.error_generic),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        ) {
            Text(stringResource(R.string.request_submit))
        }
    }
}

@Composable
private fun ServiceDropdown(
    selected: ServiceId,
    onSelected: (ServiceId) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        OutlinedTextField(
            value = stringResource(selected.titleRes()),
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.nav_services)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
        )
        androidx.compose.material3.ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            ServiceId.entries.forEach { id ->
                DropdownMenuItem(
                    text = { Text(stringResource(id.titleRes())) },
                    onClick = {
                        onSelected(id)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun errorTextFor(state: RequestFormState, field: RequestField): @Composable (() -> Unit)? {
    if (state.errorField != field) return null
    val resId = when (state.genericErrorKey) {
        "full_name_required" -> R.string.error_full_name_required
        "contact_required" -> R.string.error_contact_required
        "invalid_email" -> R.string.error_invalid_email
        "message_too_short" -> R.string.error_message_too_short
        else -> return null
    }
    return { Text(stringResource(resId), color = MaterialTheme.colorScheme.error) }
}

@Composable
private fun SuccessView(modifier: Modifier, onClose: () -> Unit) {
    Column(
        modifier = modifier.padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Outlined.CheckCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(96.dp),
        )
        Text(
            text = stringResource(R.string.request_submitted),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 16.dp),
        )
        TextButton(
            onClick = onClose,
            modifier = Modifier.padding(top = 16.dp),
        ) {
            Text(stringResource(R.string.action_close))
        }
    }
}
