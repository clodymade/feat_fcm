/**
 * File: HiApplication.kt
 *
 * Description: This class initializes global settings and handles Firebase Cloud Messaging (FCM) token management.
 *              It extends the Android Application class and ensures that Firebase is properly configured at app startup.
 *
 * Author: netcanis
 * Created: 2024-12-26
 *
 * License: MIT
 */

package com.mwkg.fcm

import android.app.Application
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging

/**
 * Application class for initializing global configurations and Firebase Cloud Messaging (FCM).
 * This class runs before any Activity or Service is created.
 */
class HiApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase and fetch the FCM token
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("ModularX::HiApplication", "FCM Token: $token")
            } else {
                Log.w("ModularX::HiApplication", "Fetching FCM token failed", task.exception)
            }
        }
    }
}
