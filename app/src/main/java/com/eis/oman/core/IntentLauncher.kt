package com.eis.oman.core

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IntentLauncher @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    fun openWhatsApp(phoneE164: String, prefilledMessage: String): LaunchOutcome {
        val sanitised = phoneE164.removePrefix("+").filter { it.isDigit() }
        val encoded = Uri.encode(prefilledMessage)
        val uri = Uri.parse("https://wa.me/$sanitised?text=$encoded")
        return launchView(uri)
    }

    fun dial(phoneE164: String): LaunchOutcome {
        val uri = Uri.parse("tel:$phoneE164")
        return launchAction(Intent.ACTION_DIAL, uri)
    }

    fun email(address: String, subject: String, body: String): LaunchOutcome {
        val uri = Uri.parse(
            "mailto:$address?subject=${Uri.encode(subject)}&body=${Uri.encode(body)}"
        )
        return launchAction(Intent.ACTION_SENDTO, uri)
    }

    fun openUrl(url: String): LaunchOutcome = launchView(Uri.parse(url))

    private fun launchView(uri: Uri): LaunchOutcome = launchAction(Intent.ACTION_VIEW, uri)

    private fun launchAction(action: String, uri: Uri): LaunchOutcome {
        val intent = Intent(action, uri).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return try {
            context.startActivity(intent)
            LaunchOutcome.Launched
        } catch (_: ActivityNotFoundException) {
            LaunchOutcome.NoHandler
        } catch (t: Throwable) {
            LaunchOutcome.Error(t)
        }
    }
}

sealed interface LaunchOutcome {
    data object Launched : LaunchOutcome
    data object NoHandler : LaunchOutcome
    data class Error(val cause: Throwable) : LaunchOutcome
}
