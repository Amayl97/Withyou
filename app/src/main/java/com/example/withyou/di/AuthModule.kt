package com.example.withyou.di

import com.example.withyou.authentication.data.AuthenticationRepository
import com.example.withyou.authentication.data.AuthenticationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract  class AuthModule{

    @Binds
    @Singleton
    abstract fun bindAuthenticationRespository(
        authenticationRepositoryImpl1 : AuthenticationRepositoryImpl
    ): AuthenticationRepository
}