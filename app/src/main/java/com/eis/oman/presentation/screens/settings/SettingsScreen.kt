package com.eis.oman.presentation.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eis.oman.R
import com.eis.oman.domain.model.AppLanguage
import com.eis.oman.domain.model.ThemeMode

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        containerColor = androidx.compose.ui.graphics.Color.Transparent,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.nav_settings),
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            SectionTitle(stringResource(R.string.settings_language))
            RadioRow(
                label = stringResource(R.string.settings_language_en),
                selected = state.language == AppLanguage.ENGLISH,
                onClick = { viewModel.selectLanguage(AppLanguage.ENGLISH) },
            )
            RadioRow(
                label = stringResource(R.string.settings_language_ar),
                selected = state.language == AppLanguage.ARABIC,
                onClick = { viewModel.selectLanguage(AppLanguage.ARABIC) },
            )

            SectionTitle(stringResource(R.string.settings_theme))
            RadioRow(
                label = stringResource(R.string.settings_theme_system),
                selected = state.themeMode == ThemeMode.SYSTEM,
                onClick = { viewModel.selectTheme(ThemeMode.SYSTEM) },
            )
            RadioRow(
                label = stringResource(R.string.settings_theme_light),
                selected = state.themeMode == ThemeMode.LIGHT,
                onClick = { viewModel.selectTheme(ThemeMode.LIGHT) },
            )
            RadioRow(
                label = stringResource(R.string.settings_theme_dark),
                selected = state.themeMode == ThemeMode.DARK,
                onClick = { viewModel.selectTheme(ThemeMode.DARK) },
            )
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun RadioRow(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, onClick = onClick, role = Role.RadioButton)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        RadioButton(selected = selected, onClick = null)
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
    }
}
