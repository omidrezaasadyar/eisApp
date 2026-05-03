package com.eis.oman.presentation.screens.home

import androidx.lifecycle.ViewModel
import com.eis.oman.core.IntentLauncher
import com.eis.oman.core.LaunchOutcome
import com.eis.oman.domain.model.ContactInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

sealed interface HomeEffect {
    data object NoHandler : HomeEffect
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    val contactInfo: ContactInfo,
    private val intentLauncher: IntentLauncher,
) : ViewModel() {

    private val _effects = Channel<HomeEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onCallClick() {
        report(intentLauncher.dial(contactInfo.phoneE164))
    }

    fun onWebsiteClick() {
        report(intentLauncher.openUrl(contactInfo.websiteUrl))
    }

    fun onEmailClick(subject: String) {
        report(intentLauncher.email(contactInfo.email, subject, ""))
    }

    private fun report(outcome: LaunchOutcome) {
        if (outcome is LaunchOutcome.NoHandler) _effects.trySend(HomeEffect.NoHandler)
    }
}
