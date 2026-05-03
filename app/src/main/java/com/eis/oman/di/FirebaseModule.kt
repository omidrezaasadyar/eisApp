package com.eis.oman.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.persistentCacheSettings
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = Firebase.firestore.apply {
        firestoreSettings = firestoreSettings {
            setLocalCacheSettings(persistentCacheSettings {})
        }
    }

    @Provides
    @Singleton
    fun provideAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideMessaging(): FirebaseMessaging = FirebaseMessaging.getInstance()
}
