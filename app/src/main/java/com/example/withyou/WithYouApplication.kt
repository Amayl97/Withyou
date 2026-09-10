package com.example.withyou

import android.app.Application
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WithYouApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Purchases.configure(
            PurchasesConfiguration.Builder(
                this,
                BuildConfig.REVENUECAT_TEST_API_KEY
            ).build()
        )
    }
}