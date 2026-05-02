package com.eis.oman.presentation.screens.services

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material.icons.outlined.Engineering
import androidx.compose.material.icons.outlined.Factory
import androidx.compose.material.icons.outlined.Hub
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.PrecisionManufacturing
import androidx.compose.material.icons.outlined.Terminal
import androidx.compose.ui.graphics.vector.ImageVector
import com.eis.oman.R
import com.eis.oman.domain.model.ServiceId

@StringRes
fun ServiceId.titleRes(): Int = when (this) {
    ServiceId.INDUSTRIAL_SOFTWARE -> R.string.service_industrial_software
    ServiceId.AUTOMATION -> R.string.service_automation
    ServiceId.AI_DATA -> R.string.service_ai_data
    ServiceId.INTEGRATION_MIDDLEWARE -> R.string.service_integration_middleware
    ServiceId.ENGINEERING_CONSULTING -> R.string.service_engineering_consulting
    ServiceId.MONITORING_KPI -> R.string.service_monitoring_kpi
    ServiceId.EQUIPMENT_TRADING -> R.string.service_equipment_trading
    ServiceId.INSTALLATION_COMMISSIONING -> R.string.service_installation_commissioning
    ServiceId.SMART_FACTORY -> R.string.service_smart_factory
}

@StringRes
fun ServiceId.shortRes(): Int = when (this) {
    ServiceId.INDUSTRIAL_SOFTWARE -> R.string.service_industrial_software_short
    ServiceId.AUTOMATION -> R.string.service_automation_short
    ServiceId.AI_DATA -> R.string.service_ai_data_short
    ServiceId.INTEGRATION_MIDDLEWARE -> R.string.service_integration_middleware_short
    ServiceId.ENGINEERING_CONSULTING -> R.string.service_engineering_consulting_short
    ServiceId.MONITORING_KPI -> R.string.service_monitoring_kpi_short
    ServiceId.EQUIPMENT_TRADING -> R.string.service_equipment_trading_short
    ServiceId.INSTALLATION_COMMISSIONING -> R.string.service_installation_commissioning_short
    ServiceId.SMART_FACTORY -> R.string.service_smart_factory_short
}

@StringRes
fun ServiceId.descRes(): Int = when (this) {
    ServiceId.INDUSTRIAL_SOFTWARE -> R.string.service_industrial_software_desc
    ServiceId.AUTOMATION -> R.string.service_automation_desc
    ServiceId.AI_DATA -> R.string.service_ai_data_desc
    ServiceId.INTEGRATION_MIDDLEWARE -> R.string.service_integration_middleware_desc
    ServiceId.ENGINEERING_CONSULTING -> R.string.service_engineering_consulting_desc
    ServiceId.MONITORING_KPI -> R.string.service_monitoring_kpi_desc
    ServiceId.EQUIPMENT_TRADING -> R.string.service_equipment_trading_desc
    ServiceId.INSTALLATION_COMMISSIONING -> R.string.service_installation_commissioning_desc
    ServiceId.SMART_FACTORY -> R.string.service_smart_factory_desc
}

fun ServiceId.icon(): ImageVector = when (this) {
    ServiceId.INDUSTRIAL_SOFTWARE -> Icons.Outlined.Terminal
    ServiceId.AUTOMATION -> Icons.Outlined.PrecisionManufacturing
    ServiceId.AI_DATA -> Icons.Outlined.Insights
    ServiceId.INTEGRATION_MIDDLEWARE -> Icons.Outlined.Hub
    ServiceId.ENGINEERING_CONSULTING -> Icons.Outlined.Engineering
    ServiceId.MONITORING_KPI -> Icons.Outlined.Analytics
    ServiceId.EQUIPMENT_TRADING -> Icons.Outlined.LocalShipping
    ServiceId.INSTALLATION_COMMISSIONING -> Icons.Outlined.Construction
    ServiceId.SMART_FACTORY -> Icons.Outlined.Factory
}
