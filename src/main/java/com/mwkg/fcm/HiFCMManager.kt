/**
 * File: HiFCMManager.kt
 *
 * Description: This class handles Firebase Cloud Messaging (FCM) services, including receiving and managing
 *              both notification and data messages. It also handles token updates and manages the creation
 *              of notifications and notification channels.
 *
 * Author: netcanis
 * Created: 2024-12-26
 *
 * License: MIT
 *
 * References:
 * 1. Firebase Cloud Messaging (FCM) Documentation:
 *    https://firebase.google.com/docs/cloud-messaging
 * 2. Android Notification Guide:
 *    https://developer.android.com/training/notify-user/build-notification
 * 3. PendingIntent Flags Documentation:
 *    https://developer.android.com/reference/android/app/PendingIntent
 */

package com.mwkg.fcm

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Firebase Cloud Messaging (FCM) service class.
 * Handles receiving messages, processing data payloads, and displaying notifications.
 */
class HiFCMManager : FirebaseMessagingService() {

    companion object {
        /**
         * Target Activity to handle FCM-related actions. This should be set by the app module.
         */
        var activity: Class<*>? = null

        /**
         * Key for passing the FCM token to the target Activity.
         */
        const val TOKEN_EXTRA = "FCM_TOKEN_EXTRA"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel() // Create the notification channel (required for Android 8.0+)
    }

    /**
     * Called when an FCM message is received.
     * Handles both notification and data payloads.
     *
     * @param remoteMessage The message received from FCM.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("ModularX::FCM", "From: ${remoteMessage.from}")

        // Handle notification messages
        remoteMessage.notification?.let {
            val title = it.title ?: "Notification"
            val message = it.body ?: "You have a new message."
            sendNotification(title, message)
        }

        // Handle data payload messages
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("ModularX::FCM", "Data Payload: ${remoteMessage.data}")
            val data = remoteMessage.data["message"] ?: "You have a new data message."
            sendNotification("Data Message", data)
        }
    }

    /**
     * Sends a notification to the user.
     *
     * @param title The title of the notification.
     * @param message The message content of the notification.
     */
    private fun sendNotification(title: String, message: String) {
        val activityClass = activity ?: run {
            Log.e("ModularX::FCM", "targetActivity is not set.")
            return
        }

        val intent = Intent(this, activityClass).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(this, "default")
            .setSmallIcon(R.drawable.ic_notification) // Notification icon
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // Check notification permission and display the notification
        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@HiFCMManager,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.e("ModularX::FCM", "Missing POST_NOTIFICATIONS permission. Unable to display notification.")
                return
            }
            notify(0, builder.build())
        }
    }

    /**
     * Creates the notification channel required for Android 8.0 and above.
     */
    private fun createNotificationChannel() {
        val name = "Default Channel"
        val descriptionText = "Default Channel for App"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("default", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    /**
     * Called when a new FCM token is generated or updated.
     *
     * @param token The new FCM token.
     */
    override fun onNewToken(token: String) {
        Log.d("ModularX::FCM", "New FCM Token: $token")
        handleToken(token)
    }

    /**
     * Handles the new or updated FCM token.
     * Sends the token to the target Activity if set.
     *
     * @param token The FCM token to handle.
     */
    private fun handleToken(token: String) {
        activity?.let { activityClass ->
            val intent = Intent(this, activityClass).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(TOKEN_EXTRA, token) // Pass the token to the Activity
            }
            startActivity(intent)
        } ?: Log.e("ModularX::FCM", "targetActivity is not set.")
    }
}
