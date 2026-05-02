package com.eis.oman

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.core.content.getSystemService
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EISApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initFirebase()
        registerNotificationChannel()
    }

    private fun initFirebase() {
        Firebase.crashlytics.isCrashlyticsCollectionEnabled = !BuildConfig.DEBUG
        Firebase.analytics.setAnalyticsCollectionEnabled(!BuildConfig.DEBUG)
    }

    private fun registerNotificationChannel() {
        val manager = getSystemService<NotificationManager>() ?: return
        val channel = NotificationChannel(
            getString(R.string.notif_channel_default_id),
            getString(R.string.notif_channel_default_name),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = getString(R.string.notif_channel_default_desc)
        }
        manager.createNotificationChannel(channel)
    }
}
