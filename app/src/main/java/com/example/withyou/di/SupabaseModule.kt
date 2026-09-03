package com.example.withyou.di

import android.util.Log
import com.example.withyou.BuildConfig
import com.example.withyou.authentication.data.AuthenticationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(
        authenticationRepository: AuthenticationRepository
    ): SupabaseClient {

        return createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_KEY
        ) {

            accessToken = {
                Log.d(
                    "SUPABASE_AUTH_DEBUG",
                    "Supabase requesting Firebase token"
                )

                authenticationRepository.getFirebaseIdToken()
            }

            install(Storage)
        }
    }
}