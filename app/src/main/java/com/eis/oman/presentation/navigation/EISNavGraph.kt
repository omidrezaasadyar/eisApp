package com.eis.oman.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.eis.oman.presentation.screens.about.AboutScreen
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
                onOpenServices = { navController.navigate(EISRoute.Services.path) },
                onOpenAbout = { navController.navigate(EISRoute.About.path) },
                onOpenContact = { navController.navigate(EISRoute.Contact.path) },
                onOpenSettings = { navController.navigate(EISRoute.Settings.path) },
                onOpenRequest = { navController.navigate(EISRoute.Request.build()) },
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
    }
}
