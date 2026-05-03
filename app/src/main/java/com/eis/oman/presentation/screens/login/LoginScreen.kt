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
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.eis.oman.domain.model.User

@Composable
fun LoginScreen(
    onBack: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        containerColor = Color.Transparent,
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
        if (state.signedInUser != null) {
            SignedInView(
                user = state.signedInUser!!,
                onSignOut = viewModel::signOut,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            )
        } else {
            FormView(
                form = state.form,
                viewModel = viewModel,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            )
        }
    }

    ForgotPasswordDialog(
        state = state.forgotPassword,
        onDismiss = viewModel::dismissForgotPassword,
        onAcknowledge = viewModel::acknowledgeForgotPasswordSent,
        onEmailChange = viewModel::onForgotEmailChange,
        onSubmit = viewModel::submitForgotPassword,
    )
}

@Composable
private fun FormView(
    form: LoginFormState,
    viewModel: LoginViewModel,
    modifier: Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HeroBadge()

        Spacer(Modifier.height(20.dp))

        Text(
            text = stringResource(
                if (form.mode == LoginMode.SIGN_IN) R.string.login_title
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
                if (form.mode == LoginMode.SIGN_IN) R.string.login_subtitle
                else R.string.signup_subtitle
            ),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(20.dp))

        ModeSelector(mode = form.mode, onModeChange = viewModel::setMode)

        Spacer(Modifier.height(20.dp))

        AuthErrorBanner(error = form.authError, onDismiss = viewModel::dismissAuthError)

        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                if (form.mode == LoginMode.SIGN_IN) {
                    SignInFields(form = form, viewModel = viewModel)
                } else {
                    SignUpFields(form = form, viewModel = viewModel)
                }

                Spacer(Modifier.height(4.dp))

                Button(
                    onClick = viewModel::submit,
                    enabled = !form.isSubmitting,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    AnimatedVisibility(
                        visible = form.isSubmitting,
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(18.dp),
                        )
                    }
                    if (!form.isSubmitting) {
                        Text(
                            text = stringResource(
                                if (form.mode == LoginMode.SIGN_IN) R.string.login_button_signin
                                else R.string.login_button_signup
                            ),
                        )
                    }
                }

                if (form.mode == LoginMode.SIGN_IN) {
                    TextButton(
                        onClick = viewModel::showForgotPassword,
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
                    if (form.mode == LoginMode.SIGN_IN) LoginMode.SIGN_UP
                    else LoginMode.SIGN_IN
                )
            }
        ) {
            Text(
                text = stringResource(
                    if (form.mode == LoginMode.SIGN_IN) R.string.login_switch_to_signup
                    else R.string.login_switch_to_signin
                ),
                color = MaterialTheme.colorScheme.primary,
            )
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun SignedInView(
    user: User,
    onSignOut: () -> Unit,
    modifier: Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(40.dp))

        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(96.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(56.dp),
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        val displayName = listOf(user.firstName, user.lastName)
            .filter { it.isNotBlank() }
            .joinToString(" ")
            .ifBlank { user.email }
        Text(
            text = stringResource(R.string.dashboard_welcome, displayName),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
        )
        if (user.email.isNotBlank()) {
            Spacer(Modifier.height(6.dp))
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }

        Spacer(Modifier.height(28.dp))

        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = stringResource(R.string.dashboard_placeholder),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        OutlinedButton(
            onClick = onSignOut,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(
                imageVector = Icons.Outlined.Logout,
                contentDescription = null,
            )
            Spacer(Modifier.size(8.dp))
            Text(stringResource(R.string.dashboard_sign_out))
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
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
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
private fun AuthErrorBanner(error: AuthBannerKind?, onDismiss: () -> Unit) {
    if (error == null) return
    val resId = when (error) {
        AuthBannerKind.InvalidCredentials -> R.string.auth_error_invalid_credentials
        AuthBannerKind.UserNotFound -> R.string.auth_error_user_not_found
        AuthBannerKind.EmailAlreadyInUse -> R.string.auth_error_email_in_use
        AuthBannerKind.WeakPassword -> R.string.auth_error_weak_password
        AuthBannerKind.InvalidEmail -> R.string.auth_error_invalid_email
        AuthBannerKind.Network -> R.string.auth_error_network
        AuthBannerKind.Unknown -> R.string.auth_error_unknown
    }
    Surface(
        color = MaterialTheme.colorScheme.errorContainer,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
    ) {
        Text(
            text = stringResource(resId),
            color = MaterialTheme.colorScheme.onErrorContainer,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp),
        )
    }
}

@Composable
private fun SignInFields(form: LoginFormState, viewModel: LoginViewModel) {
    LoginInputField(
        value = form.username,
        onValueChange = viewModel::onUsernameChange,
        label = stringResource(R.string.login_field_username),
        leadingIcon = Icons.Outlined.AlternateEmail,
        keyboardType = KeyboardType.Email,
        imeAction = ImeAction.Next,
        error = form.fieldErrors[LoginField.Username],
    )
    PasswordField(
        value = form.password,
        onValueChange = viewModel::onPasswordChange,
        visible = form.passwordVisible,
        onToggleVisibility = viewModel::togglePasswordVisible,
        error = form.fieldErrors[LoginField.Password],
        imeAction = ImeAction.Done,
    )
}

@Composable
private fun SignUpFields(form: LoginFormState, viewModel: LoginViewModel) {
    LoginInputField(
        value = form.firstName,
        onValueChange = viewModel::onFirstNameChange,
        label = stringResource(R.string.login_field_first_name),
        leadingIcon = Icons.Outlined.Person,
        imeAction = ImeAction.Next,
        error = form.fieldErrors[LoginField.FirstName],
    )
    LoginInputField(
        value = form.lastName,
        onValueChange = viewModel::onLastNameChange,
        label = stringResource(R.string.login_field_last_name),
        leadingIcon = Icons.Outlined.Badge,
        imeAction = ImeAction.Next,
        error = form.fieldErrors[LoginField.LastName],
    )
    LoginInputField(
        value = form.email,
        onValueChange = viewModel::onEmailChange,
        label = stringResource(R.string.login_field_email),
        leadingIcon = Icons.Outlined.AlternateEmail,
        keyboardType = KeyboardType.Email,
        imeAction = ImeAction.Next,
        error = form.fieldErrors[LoginField.Email],
    )
    LoginInputField(
        value = form.phone,
        onValueChange = viewModel::onPhoneChange,
        label = stringResource(R.string.login_field_phone),
        leadingIcon = Icons.Outlined.Phone,
        keyboardType = KeyboardType.Phone,
        imeAction = ImeAction.Next,
        error = form.fieldErrors[LoginField.Phone],
    )
    PasswordField(
        value = form.password,
        onValueChange = viewModel::onPasswordChange,
        visible = form.passwordVisible,
        onToggleVisibility = viewModel::togglePasswordVisible,
        error = form.fieldErrors[LoginField.Password],
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
private fun ForgotPasswordDialog(
    state: ForgotPasswordState,
    onDismiss: () -> Unit,
    onAcknowledge: () -> Unit,
    onEmailChange: (String) -> Unit,
    onSubmit: () -> Unit,
) {
    if (!state.visible) return

    if (state.sentSuccessfully) {
        AlertDialog(
            onDismissRequest = onAcknowledge,
            icon = {
                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            },
            title = { Text(stringResource(R.string.forgot_password_success_title)) },
            text = {
                Text(
                    stringResource(R.string.forgot_password_success_body, state.email),
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            confirmButton = {
                TextButton(onClick = onAcknowledge) {
                    Text(stringResource(R.string.action_close))
                }
            },
        )
        return
    }

    val errorTextResId = when (state.authError) {
        AuthBannerKind.UserNotFound -> R.string.auth_error_user_not_found
        AuthBannerKind.InvalidEmail -> R.string.auth_error_invalid_email
        AuthBannerKind.Network -> R.string.auth_error_network
        AuthBannerKind.Unknown,
        AuthBannerKind.InvalidCredentials,
        AuthBannerKind.EmailAlreadyInUse,
        AuthBannerKind.WeakPassword -> R.string.auth_error_unknown
        null -> null
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Outlined.Lock,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        },
        title = { Text(stringResource(R.string.forgot_password_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = stringResource(R.string.forgot_password_body),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                OutlinedTextField(
                    value = state.email,
                    onValueChange = onEmailChange,
                    label = { Text(stringResource(R.string.login_field_email)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.AlternateEmail,
                            contentDescription = null,
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done,
                    ),
                    isError = state.emailError != null,
                    supportingText = errorSupportingText(state.emailError),
                    enabled = !state.isSending,
                    modifier = Modifier.fillMaxWidth(),
                )
                if (errorTextResId != null) {
                    Text(
                        text = stringResource(errorTextResId),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onSubmit,
                enabled = !state.isSending,
            ) {
                if (state.isSending) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(16.dp),
                    )
                } else {
                    Text(stringResource(R.string.forgot_password_send))
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !state.isSending) {
                Text(stringResource(R.string.forgot_password_cancel))
            }
        },
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
        LoginErrorKind.NotEnglish -> R.string.login_english_only
    }
    return { Text(stringResource(resId), color = MaterialTheme.colorScheme.error) }
}
