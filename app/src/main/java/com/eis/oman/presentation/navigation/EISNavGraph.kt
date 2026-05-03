package com.eis.oman.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Hub
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Workspaces
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.eis.oman.R
import com.eis.oman.presentation.screens.about.AboutScreen
import com.eis.oman.presentation.screens.coming_soon.ComingSoonScreen
import com.eis.oman.presentation.screens.contact.ContactScreen
import com.eis.oman.presentation.screens.home.HomeScreen
import com.eis.oman.presentation.screens.request.RequestScreen
import com.eis.oman.presentation.screens.service_detail.ServiceDetailScreen
import com.eis.oman.presentation.screens.services.ServicesScreen
import com.eis.oman.presentation.screens.settings.SettingsScreen
import com.eis.oman.presentation.screens.splash.SplashScreen

@Composable
fun EISNavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = EISRoute.Splash.path) {

        composable(EISRoute.Splash.path) {
            SplashScreen(
                onReady = {
                    navController.navigate(EISRoute.Home.path) {
                        popUpTo(EISRoute.Splash.path) { inclusive = true }
                    }
                }
            )
        }

        composable(EISRoute.Home.path) {
            HomeScreen(
                onOpenRoute = { route ->
                    navController.navigate(route)
                },
            )
        }

        composable(EISRoute.Services.path) {
            ServicesScreen(
                onBack = { navController.popBackStack() },
                onOpenService = { id -> navController.navigate(EISRoute.ServiceDetail.build(id.key)) },
            )
        }

        composable(
            route = EISRoute.ServiceDetail.path,
            arguments = listOf(navArgument(EISRoute.ServiceDetail.ARG_SERVICE_ID) {
                type = NavType.StringType
            })
        ) { entry ->
            val key = entry.arguments?.getString(EISRoute.ServiceDetail.ARG_SERVICE_ID).orEmpty()
            ServiceDetailScreen(
                serviceKey = key,
                onBack = { navController.popBackStack() },
                onRequest = { navController.navigate(EISRoute.Request.build(key)) },
            )
        }

        composable(
            route = EISRoute.Request.path,
            arguments = listOf(navArgument(EISRoute.Request.ARG_SERVICE_ID) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { entry ->
            val key = entry.arguments?.getString(EISRoute.Request.ARG_SERVICE_ID)
            RequestScreen(
                preselectedServiceKey = key,
                onBack = { navController.popBackStack() },
                onSubmitted = { navController.popBackStack() },
            )
        }

        composable(EISRoute.About.path) {
            AboutScreen(onBack = { navController.popBackStack() })
        }

        composable(EISRoute.Contact.path) {
            ContactScreen(onBack = { navController.popBackStack() })
        }

        composable(EISRoute.Settings.path) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }

        composable(EISRoute.Projects.path) {
            ComingSoonScreen(
                titleRes = R.string.nav_projects,
                icon = Icons.Outlined.Workspaces,
                onBack = { navController.popBackStack() },
            )
        }
        composable(EISRoute.Dashboard.path) {
            ComingSoonScreen(
                titleRes = R.string.nav_dashboard,
                icon = Icons.Outlined.Dashboard,
                onBack = { navController.popBackStack() },
            )
        }
        composable(EISRoute.News.path) {
            ComingSoonScreen(
                titleRes = R.string.nav_news,
                icon = Icons.Outlined.Campaign,
                onBack = { navController.popBackStack() },
            )
        }
        composable(EISRoute.TechHub.path) {
            ComingSoonScreen(
                titleRes = R.string.nav_tech_hub,
                icon = Icons.Outlined.Hub,
                onBack = { navController.popBackStack() },
            )
        }
        composable(EISRoute.Agent.path) {
            ComingSoonScreen(
                titleRes = R.string.nav_agent,
                icon = Icons.Outlined.Psychology,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
