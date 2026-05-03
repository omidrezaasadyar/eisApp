package com.eis.oman.presentation.screens.home

import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Hub
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.MiscellaneousServices
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Workspaces
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHost) },
        contentWindowInsets = WindowInsets(0),
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawHomeGrid(spacingPx = 32.dp.toPx(), alpha = 0.04f)
            }

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
            .height(70.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxHeight(),
        ) {
            Box(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_eis_logo),
                    contentDescription = stringResource(R.string.app_name),
                    modifier = Modifier.size(width = 50.dp, height = 36.dp),
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF2A1F1A),
                            MaterialTheme.colorScheme.surface,
                        ),
                        radius = 600f,
                    )
                ),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .clip(RoundedCornerShape(110.dp))
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.18f),
                                Color.Transparent,
                            ),
                        )
                    ),
            )
            Image(
                painter = painterResource(R.drawable.ic_eis_logo),
                contentDescription = null,
                modifier = Modifier.size(width = 200.dp, height = 140.dp),
                contentScale = ContentScale.Fit,
            )
        }
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
            left = MenuItem(R.string.nav_request, Icons.Outlined.Description, EISRoute.Request.build()),
            right = MenuItem(R.string.nav_services, Icons.Outlined.MiscellaneousServices, EISRoute.Services.path),
            onOpen = onOpen,
            tall = false,
        )
        MenuRow(
            left = MenuItem(R.string.nav_projects, Icons.Outlined.Workspaces, EISRoute.Projects.path),
            right = MenuItem(R.string.nav_dashboard, Icons.Outlined.Dashboard, EISRoute.Dashboard.path),
            onOpen = onOpen,
            tall = true,
        )
        MenuRow(
            left = MenuItem(R.string.nav_news, Icons.Outlined.Campaign, EISRoute.News.path),
            right = MenuItem(R.string.nav_tech_hub, Icons.Outlined.Hub, EISRoute.TechHub.path),
            onOpen = onOpen,
            tall = true,
        )
        MenuRow(
            left = MenuItem(R.string.nav_settings, Icons.Outlined.Settings, EISRoute.Settings.path),
            right = MenuItem(R.string.nav_agent, Icons.Outlined.Psychology, EISRoute.Agent.path),
            onOpen = onOpen,
            tall = true,
        )
    }
}

private data class MenuItem(
    val labelRes: Int,
    val icon: ImageVector,
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
        MenuCard(
            item = left,
            modifier = Modifier
                .weight(1f)
                .height(if (tall) 79.dp else 73.dp),
            onClick = { onOpen(left.route) },
        )
        MenuCard(
            item = right,
            modifier = Modifier
                .weight(1f)
                .height(if (tall) 79.dp else 73.dp),
            onClick = { onOpen(right.route) },
        )
    }
}

@Composable
private fun MenuCard(
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
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(item.labelRes),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
            )
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(36.dp),
            )
        }
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

private fun DrawScope.drawHomeGrid(spacingPx: Float, alpha: Float) {
    val color = Color.White.copy(alpha = alpha)
    val w = size.width
    val h = size.height
    var x = 0f
    while (x <= w) {
        drawLine(color = color, start = Offset(x, 0f), end = Offset(x, h), strokeWidth = 0.5f)
        x += spacingPx
    }
    var y = 0f
    while (y <= h) {
        drawLine(color = color, start = Offset(0f, y), end = Offset(w, y), strokeWidth = 0.5f)
        y += spacingPx
    }
}
