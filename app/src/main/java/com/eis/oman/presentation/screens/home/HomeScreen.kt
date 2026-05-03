package com.eis.oman.presentation.screens.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eis.oman.R
import com.eis.oman.presentation.navigation.EISRoute

@Composable
fun HomeScreen(
    onOpenRoute: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val snackbarHost = remember { SnackbarHostState() }
    val noHandlerMessage = stringResource(R.string.error_no_handler)
    val emailSubject = stringResource(R.string.brand_full_name)

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            if (effect == HomeEffect.NoHandler) {
                snackbarHost.showSnackbar(noHandlerMessage)
            }
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        snackbarHost = { SnackbarHost(snackbarHost) },
        contentWindowInsets = WindowInsets(0),
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
            ) {
                Spacer(Modifier.height(8.dp))
                TopBar()
                Spacer(Modifier.height(8.dp))
                HeroCard()
                Spacer(Modifier.height(8.dp))
                MenuGrid(onOpen = onOpenRoute)
                Spacer(Modifier.height(8.dp))
                BottomActionBar(
                    onCall = viewModel::onCallClick,
                    onWebsite = viewModel::onWebsiteClick,
                    onEmail = { viewModel.onEmailClick(emailSubject) },
                    onAbout = { onOpenRoute(EISRoute.About.path) },
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(82.dp)
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxHeight(),
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_eis_logo),
                    contentDescription = stringResource(R.string.app_name),
                    modifier = Modifier.fillMaxHeight(),
                    contentScale = ContentScale.Fit,
                )
            }
        }
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.brand_full_name),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Light,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false,
                )
            }
        }
    }
}

@Composable
private fun HeroCard() {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp),
    ) {
        Image(
            painter = painterResource(R.drawable.home_hero),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun MenuGrid(onOpen: (String) -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        MenuRow(
            left = MenuItem(R.string.nav_request, R.drawable.ic_menu_request, EISRoute.Request.build()),
            right = MenuItem(R.string.nav_services, R.drawable.ic_menu_services, EISRoute.Services.path),
            onOpen = onOpen,
            tall = false,
        )
        MenuRow(
            left = MenuItem(R.string.nav_projects, R.drawable.ic_menu_projects, EISRoute.Projects.path),
            right = MenuItem(R.string.nav_dashboard, R.drawable.ic_menu_dashboard, EISRoute.Dashboard.path),
            onOpen = onOpen,
            tall = true,
        )
        MenuRow(
            left = MenuItem(R.string.nav_news, R.drawable.ic_menu_news, EISRoute.News.path),
            right = MenuItem(R.string.nav_tech_hub, R.drawable.ic_menu_tech_hub, EISRoute.TechHub.path),
            onOpen = onOpen,
            tall = true,
        )
        MenuRow(
            left = MenuItem(R.string.nav_settings, R.drawable.ic_menu_settings, EISRoute.Settings.path),
            right = MenuItem(R.string.nav_agent, R.drawable.ic_menu_agent, EISRoute.Agent.path),
            onOpen = onOpen,
            tall = true,
        )
    }
}

private data class MenuItem(
    val labelRes: Int,
    @DrawableRes val iconRes: Int,
    val route: String,
)

@Composable
private fun MenuRow(
    left: MenuItem,
    right: MenuItem,
    onOpen: (String) -> Unit,
    tall: Boolean,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        MenuButton(
            item = left,
            modifier = Modifier
                .weight(1f)
                .height(if (tall) 79.dp else 73.dp),
            onClick = { onOpen(left.route) },
        )
        MenuButton(
            item = right,
            modifier = Modifier
                .weight(1f)
                .height(if (tall) 79.dp else 73.dp),
            onClick = { onOpen(right.route) },
        )
    }
}

@Composable
private fun MenuButton(
    item: MenuItem,
    modifier: Modifier,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier,
    ) {
        Image(
            painter = painterResource(item.iconRes),
            contentDescription = stringResource(item.labelRes),
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun BottomActionBar(
    onCall: () -> Unit,
    onWebsite: () -> Unit,
    onEmail: () -> Unit,
    onAbout: () -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(68.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ActionPill(icon = Icons.Outlined.Call, onClick = onCall, modifier = Modifier.weight(1f))
            ActionPill(icon = Icons.Outlined.Language, onClick = onWebsite, modifier = Modifier.weight(1f))
            ActionPill(icon = Icons.Outlined.AlternateEmail, onClick = onEmail, modifier = Modifier.weight(1f))
            ActionPill(icon = Icons.Outlined.Info, onClick = onAbout, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun ActionPill(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier,
) {
    Surface(
        onClick = onClick,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier.fillMaxHeight(),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp),
            )
        }
    }
}
