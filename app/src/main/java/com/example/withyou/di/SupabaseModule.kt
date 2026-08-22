package com.example.withyou.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.tasks.await
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = "sb_publishable_cayz1e8GYIUGvk_VWkKCVQ_he8l8_DU",
            supabaseKey = "https://nvcwzgtnpshtfrjstwda.supabase.co/rest/v1/"
        ) {

            accessToken = {
                FirebaseAuth.getInstance()
                    .currentUser
                    ?.getIdToken(false)
                    ?.await()
                    ?.token
            }

            install(Storage)
        }
    }
}