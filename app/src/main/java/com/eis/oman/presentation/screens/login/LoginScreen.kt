package com.eis.oman.presentation.screens.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eis.oman.R

@Composable
fun LoginScreen(
    onBack: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val snackbarHost = remember { SnackbarHostState() }
    val pendingMessage = stringResource(R.string.login_pending)

    LaunchedEffect(state.isSubmitted) {
        if (state.isSubmitted) {
            snackbarHost.showSnackbar(pendingMessage)
            viewModel.resetSubmittedFlag()
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        snackbarHost = { SnackbarHost(snackbarHost) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.nav_dashboard),
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HeroBadge()

            Spacer(Modifier.height(20.dp))

            Text(
                text = stringResource(
                    if (state.mode == LoginMode.SIGN_IN) R.string.login_title
                    else R.string.signup_title
                ),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = stringResource(
                    if (state.mode == LoginMode.SIGN_IN) R.string.login_subtitle
                    else R.string.signup_subtitle
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(20.dp))

            ModeSelector(
                mode = state.mode,
                onModeChange = viewModel::setMode,
            )

            Spacer(Modifier.height(20.dp))

            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    if (state.mode == LoginMode.SIGN_IN) {
                        SignInFields(state = state, viewModel = viewModel)
                    } else {
                        SignUpFields(state = state, viewModel = viewModel)
                    }

                    Spacer(Modifier.height(4.dp))

                    Button(
                        onClick = viewModel::submit,
                        enabled = !state.isSubmitting,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        AnimatedVisibility(
                            visible = state.isSubmitting,
                            enter = fadeIn(),
                            exit = fadeOut(),
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(18.dp),
                            )
                        }
                        if (!state.isSubmitting) {
                            Text(
                                text = stringResource(
                                    if (state.mode == LoginMode.SIGN_IN) R.string.login_button_signin
                                    else R.string.login_button_signup
                                ),
                            )
                        }
                    }

                    if (state.mode == LoginMode.SIGN_IN) {
                        TextButton(
                            onClick = { /* future: forgot password */ },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(stringResource(R.string.login_forgot_password))
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            TextButton(
                onClick = {
                    viewModel.setMode(
                        if (state.mode == LoginMode.SIGN_IN) LoginMode.SIGN_UP
                        else LoginMode.SIGN_IN
                    )
                }
            ) {
                Text(
                    text = stringResource(
                        if (state.mode == LoginMode.SIGN_IN) R.string.login_switch_to_signup
                        else R.string.login_switch_to_signin
                    ),
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun HeroBadge() {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .padding(top = 16.dp)
            .size(96.dp),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.Outlined.AccountCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(56.dp),
            )
        }
    }
}

@Composable
private fun ModeSelector(
    mode: LoginMode,
    onModeChange: (LoginMode) -> Unit,
) {
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth(),
    ) {
        SegmentedButton(
            selected = mode == LoginMode.SIGN_IN,
            onClick = { onModeChange(LoginMode.SIGN_IN) },
            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
        ) { Text(stringResource(R.string.login_tab_signin)) }
        SegmentedButton(
            selected = mode == LoginMode.SIGN_UP,
            onClick = { onModeChange(LoginMode.SIGN_UP) },
            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
        ) { Text(stringResource(R.string.login_tab_signup)) }
    }
}

@Composable
private fun SignInFields(state: LoginUiState, viewModel: LoginViewModel) {
    LoginInputField(
        value = state.username,
        onValueChange = viewModel::onUsernameChange,
        label = stringResource(R.string.login_field_username),
        leadingIcon = Icons.Outlined.AlternateEmail,
        keyboardType = KeyboardType.Email,
        imeAction = ImeAction.Next,
        error = state.errors[LoginField.Username],
    )
    PasswordField(
        value = state.password,
        onValueChange = viewModel::onPasswordChange,
        visible = state.passwordVisible,
        onToggleVisibility = viewModel::togglePasswordVisible,
        error = state.errors[LoginField.Password],
        imeAction = ImeAction.Done,
    )
}

@Composable
private fun SignUpFields(state: LoginUiState, viewModel: LoginViewModel) {
    LoginInputField(
        value = state.firstName,
        onValueChange = viewModel::onFirstNameChange,
        label = stringResource(R.string.login_field_first_name),
        leadingIcon = Icons.Outlined.Person,
        imeAction = ImeAction.Next,
        error = state.errors[LoginField.FirstName],
    )
    LoginInputField(
        value = state.lastName,
        onValueChange = viewModel::onLastNameChange,
        label = stringResource(R.string.login_field_last_name),
        leadingIcon = Icons.Outlined.Badge,
        imeAction = ImeAction.Next,
        error = state.errors[LoginField.LastName],
    )
    LoginInputField(
        value = state.email,
        onValueChange = viewModel::onEmailChange,
        label = stringResource(R.string.login_field_email),
        leadingIcon = Icons.Outlined.AlternateEmail,
        keyboardType = KeyboardType.Email,
        imeAction = ImeAction.Next,
        error = state.errors[LoginField.Email],
    )
    LoginInputField(
        value = state.phone,
        onValueChange = viewModel::onPhoneChange,
        label = stringResource(R.string.login_field_phone),
        leadingIcon = Icons.Outlined.Phone,
        keyboardType = KeyboardType.Phone,
        imeAction = ImeAction.Next,
        error = state.errors[LoginField.Phone],
    )
    PasswordField(
        value = state.password,
        onValueChange = viewModel::onPasswordChange,
        visible = state.passwordVisible,
        onToggleVisibility = viewModel::togglePasswordVisible,
        error = state.errors[LoginField.Password],
        imeAction = ImeAction.Done,
    )
}

@Composable
private fun LoginInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    error: LoginErrorKind? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction,
        ),
        isError = error != null,
        supportingText = errorSupportingText(error),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    visible: Boolean,
    onToggleVisibility: () -> Unit,
    error: LoginErrorKind?,
    imeAction: ImeAction,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.login_field_password)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Lock,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        trailingIcon = {
            IconButton(onClick = onToggleVisibility) {
                Icon(
                    imageVector = if (visible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                    contentDescription = stringResource(
                        if (visible) R.string.login_hide_password else R.string.login_show_password
                    ),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction,
        ),
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        isError = error != null,
        supportingText = errorSupportingText(error),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun errorSupportingText(error: LoginErrorKind?): @Composable (() -> Unit)? {
    if (error == null) return null
    val resId = when (error) {
        LoginErrorKind.Required -> R.string.login_field_required
        LoginErrorKind.InvalidEmail -> R.string.login_email_invalid
        LoginErrorKind.InvalidPhone -> R.string.login_phone_invalid
        LoginErrorKind.ShortPassword -> R.string.login_password_short
    }
    return { Text(stringResource(resId), color = MaterialTheme.colorScheme.error) }
}
