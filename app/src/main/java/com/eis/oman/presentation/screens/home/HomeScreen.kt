package com.eis.oman.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eis.oman.R
import com.eis.oman.presentation.components.PlaceholderScaffold

@Composable
fun HomeScreen(
    onOpenServices: () -> Unit,
    onOpenAbout: () -> Unit,
    onOpenContact: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenRequest: () -> Unit,
) {
    PlaceholderScaffold(
        title = stringResource(R.string.app_name),
        body = stringResource(R.string.home_intro),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Button(onClick = onOpenServices, modifier = Modifier.fillMaxWidth()) {
                androidx.compose.material3.Text(stringResource(R.string.nav_services))
            }
            Button(onClick = onOpenRequest, modifier = Modifier.fillMaxWidth()) {
                androidx.compose.material3.Text(stringResource(R.string.nav_request))
            }
            OutlinedButton(onClick = onOpenAbout, modifier = Modifier.fillMaxWidth()) {
                androidx.compose.material3.Text(stringResource(R.string.nav_about))
            }
            OutlinedButton(onClick = onOpenContact, modifier = Modifier.fillMaxWidth()) {
                androidx.compose.material3.Text(stringResource(R.string.nav_contact))
            }
            OutlinedButton(onClick = onOpenSettings, modifier = Modifier.fillMaxWidth()) {
                androidx.compose.material3.Text(stringResource(R.string.nav_settings))
            }
        }
    }
}
