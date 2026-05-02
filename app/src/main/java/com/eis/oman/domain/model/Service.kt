package com.eis.oman.domain.model

/**
 * One of the nine fixed EIS service lines. Localized copy is resolved in the
 * presentation layer via string resources keyed by [id].
 */
data class Service(
    val id: ServiceId,
    val order: Int,
)

enum class ServiceId(val key: String) {
    INDUSTRIAL_SOFTWARE("industrial_software"),
    AUTOMATION("automation"),
    AI_DATA("ai_data"),
    INTEGRATION_MIDDLEWARE("integration_middleware"),
    ENGINEERING_CONSULTING("engineering_consulting"),
    MONITORING_KPI("monitoring_kpi"),
    EQUIPMENT_TRADING("equipment_trading"),
    INSTALLATION_COMMISSIONING("installation_commissioning"),
    SMART_FACTORY("smart_factory");

    companion object {
        fun fromKey(key: String): ServiceId? = entries.firstOrNull { it.key == key }
    }
}
