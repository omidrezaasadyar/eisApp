package com.eis.oman.presentation.navigation

sealed class EISRoute(val path: String) {
    data object Splash : EISRoute("splash")
    data object Home : EISRoute("home")
    data object Services : EISRoute("services")
    data object Projects : EISRoute("projects")
    data object Dashboard : EISRoute("dashboard")
    data object News : EISRoute("news")
    data object TechHub : EISRoute("tech_hub")
    data object Agent : EISRoute("agent")
    data object About : EISRoute("about")
    data object Contact : EISRoute("contact")
    data object Settings : EISRoute("settings")

    data object ServiceDetail : EISRoute("service/{serviceId}") {
        const val ARG_SERVICE_ID = "serviceId"
        fun build(serviceKey: String): String = "service/$serviceKey"
    }

    data object Request : EISRoute("request?serviceId={serviceId}") {
        const val ARG_SERVICE_ID = "serviceId"
        fun build(serviceKey: String? = null): String =
            if (serviceKey == null) "request" else "request?serviceId=$serviceKey"
    }
}
