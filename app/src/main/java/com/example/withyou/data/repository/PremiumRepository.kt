package com.example.withyou.data.repository

import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesError
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.interfaces.ReceiveCustomerInfoCallback
import jakarta.inject.Inject
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class PremiumRepository @Inject constructor() {

    suspend fun isPremium(): Boolean {
        return suspendCancellableCoroutine { continuation ->

            Purchases.sharedInstance.getCustomerInfo(
                object : ReceiveCustomerInfoCallback {

                    override fun onReceived(customerInfo: CustomerInfo) {
                        val isPremium =
                            customerInfo.entitlements["withyou_pro"]?.isActive == true

                        continuation.resume(isPremium)
                    }

                    override fun onError(error: PurchasesError) {
                        continuation.resume(false)
                    }
                }
            )
        }
    }
}