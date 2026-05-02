package com.eis.oman.presentation.screens.contact

import androidx.lifecycle.ViewModel
import com.eis.oman.core.IntentLauncher
import com.eis.oman.core.LaunchOutcome
import com.eis.oman.domain.model.ContactInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

sealed interface ContactEffect {
    data object NoHandler : ContactEffect
}

@HiltViewModel
class ContactViewModel @Inject constructor(
    val contactInfo: ContactInfo,
    private val intentLauncher: IntentLauncher,
) : ViewModel() {

    private val _effects = Channel<ContactEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onWhatsAppClick(prefilledMessage: String) {
        report(intentLauncher.openWhatsApp(contactInfo.whatsappE164, prefilledMessage))
    }

    fun onCallClick() {
        report(intentLauncher.dial(contactInfo.phoneE164))
    }

    fun onEmailClick(subject: String) {
        report(intentLauncher.email(contactInfo.email, subject, ""))
    }

    fun onWebsiteClick() {
        report(intentLauncher.openUrl(contactInfo.websiteUrl))
    }

    private fun report(outcome: LaunchOutcome) {
        if (outcome is LaunchOutcome.NoHandler) _effects.trySend(ContactEffect.NoHandler)
    }
}
