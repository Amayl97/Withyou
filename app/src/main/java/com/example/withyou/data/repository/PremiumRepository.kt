package com.example.withyou.data.repository

import com.revenuecat.purchases.interfaces.PurchaseCallback
import com.revenuecat.purchases.Offerings
import com.revenuecat.purchases.interfaces.ReceiveOfferingsCallback
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesError
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.interfaces.ReceiveCustomerInfoCallback
import jakarta.inject.Inject
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import android.app.Activity
import com.revenuecat.purchases.PurchaseParams
import com.revenuecat.purchases.Package


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

    suspend fun getOfferings(): Result<Offerings> {
        return suspendCancellableCoroutine { continuation ->

            Purchases.sharedInstance.getOfferings(
                object : ReceiveOfferingsCallback {

                    override fun onReceived(offerings: Offerings) {
                        continuation.resume(Result.success(offerings))
                    }

                    override fun onError(error: PurchasesError) {
                        continuation.resume(
                            Result.failure(
                                Exception(error.message)
                            )
                        )
                    }
                }
            )
        }
    }

    suspend fun purchase(
        activity: Activity,
        packageToPurchase: Package
    ): Result<Boolean> {

        return suspendCancellableCoroutine { continuation ->

            val purchaseParams =
                PurchaseParams.Builder(
                    activity,
                    packageToPurchase
                ).build()

            Purchases.sharedInstance.purchase(
                purchaseParams,
                object : PurchaseCallback {

                    override fun onCompleted(
                        storeTransaction: com.revenuecat.purchases.models.StoreTransaction,
                        customerInfo: CustomerInfo
                    ) {

                        val isPremium =
                            customerInfo
                                .entitlements["withyou_pro"]
                                ?.isActive == true

                        continuation.resume(
                            Result.success(isPremium)
                        )
                    }

                    override fun onError(
                        error: PurchasesError,
                        userCancelled: Boolean
                    ) {

                        if (userCancelled) {

                            continuation.resume(
                                Result.success(false)
                            )

                        } else {

                            continuation.resume(
                                Result.failure(
                                    Exception(
                                        "Purchase failed: ${error.code}"
                                    )
                                )
                            )
                        }
                    }
                }
            )
        }
    }
}