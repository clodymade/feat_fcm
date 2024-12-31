# **feat_fcm**

A **feature module** for integrating Firebase Cloud Messaging (FCM) into Android apps.

---

## **Overview**

`feat_fcm` is an Android module designed to:
- Manage FCM token generation and updates.
- Handle both notification and data messages from FCM.
- Display notifications using the Android Notification API.
- Provide seamless integration with target Activities.

This module is compatible with **Android 6.0 (API 23)** and above and uses **Jetpack** and **Kotlin Coroutines** for modern Android development.

---

## **Features**

- ✅ **Token Management**: Automatically handles FCM token generation and updates.
- ✅ **Notification Handling**: Processes and displays rich notifications.
- ✅ **Data Payload Support**: Manages custom data messages from FCM.
- ✅ **Activity Integration**: Configurable target `Activity` for handling notifications and tokens.
- ✅ **Lifecycle-Aware**: Integrates with Android lifecycle components.

---

## **Requirements**

| Requirement        | Minimum Version         |
|--------------------|-------------------------|
| **Android OS**     | 6.0 (API 23)            |
| **Kotlin**         | 1.9.22                  |
| **Android Studio** | Giraffe (2022.3.1)      |
| **Gradle**         | 8.0                     |

---

## **Setup**

### **1. Add feat_fcm to Your Project**

To include `feat_fcm` via **JitPack**, follow these steps:

1. Add JitPack to your project-level `build.gradle` file:

    ```gradle
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
    ```

2. Add `feat_fcm` to your module-level `build.gradle` file:

    ```gradle
    dependencies {
        implementation 'com.github.clodymade:feat_fcm:1.0.0'
    }
    ```

3. Sync your project.


### **2. Permissions**

Add the required permissions to your AndroidManifest.xml:

```xml
<!-- Permissions for Firebase Messaging -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

<!-- Notification permission (required for Android 13+) -->
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

---

## **Usage**

### **1. Initialize HiFCMManager**

Set the target Activity for FCM handling in your Application class:

```kotlin
import android.app.Application
import com.mwkg.fcm.HiFCMManager

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Set the target Activity for notification handling
        HiFCMManager.activity = MainActivity::class.java
    }
}
```

### **2. Handle FCM Messages**

HiFCMManager automatically handles incoming FCM messages. Both notification and data messages are processed.

Example of a Custom Activity:
```kotlin
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mwkg.fcm.HiFCMManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Handle intent data passed from HiFCMManager
        intent.extras?.let { extras ->
            val token = extras.getString(HiFCMManager.TOKEN_EXTRA)
            // Use the token as needed
        }
    }
}
```

---

## **Notifications**

HiFCMManager creates and displays notifications for both notification messages and data messages. Modify the sendNotification method in HiFCMManager for custom behavior.

### **Notification Permissions**

For Android 13 (API 33) or above, ensure notification permissions are requested:

```kotlin
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

---

## **ProGuard Rules**

Add the following rules to your ProGuard file if you use FCM:

```proguard
-keep class com.google.firebase.messaging.FirebaseMessagingService { *; }
-keep class com.google.firebase.iid.** { *; }
-dontwarn com.google.firebase.iid.**
```

---

## **Example Notification Handling**

To customize notifications, modify the sendNotification method in HiFCMManager. Below is the default behavior:

```kotlin
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

    NotificationManagerCompat.from(this).notify(0, builder.build())
}
```

---

## **License**

feat_fcm is available under the MIT License. See the LICENSE file for details.

---

## **Contributing**

Contributions are welcome! To contribute:

1. Fork this repository.
2. Create a feature branch:
```
git checkout -b feature/your-feature
```
3. Commit your changes:
```
git commit -m "Add feature: description"
```
4. Push to the branch:
```
git push origin feature/your-feature
```
5. Submit a Pull Request.

---

## **Author**

### **netcanis**
iOS GitHub: https://github.com/netcanis
Android GitHub: https://github.com/clodymade

---
