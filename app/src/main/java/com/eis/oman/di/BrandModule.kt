package com.eis.oman.di

import com.eis.oman.BuildConfig
import com.eis.oman.domain.model.ContactInfo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BrandModule {

    @Provides
    @Singleton
    fun provideContactInfo(): ContactInfo = ContactInfo(
        phoneE164 = BuildConfig.EIS_PHONE_E164,
        whatsappE164 = BuildConfig.EIS_WHATSAPP_E164,
        email = BuildConfig.EIS_EMAIL,
        supportEmail = BuildConfig.EIS_EMAIL_SUPPORT,
        websiteUrl = BuildConfig.EIS_WEBSITE_URL,
    )
}
