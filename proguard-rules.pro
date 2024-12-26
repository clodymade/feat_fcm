# --- General ProGuard rules for feat_fcm module ---

# Keep all public classes and methods in the feat_fcm package
-keep public class com.mwkg.fcm.** { *; }

# Preserve annotations
-keepattributes *Annotation*

# Preserve method signatures for reflection
-keepattributes Signature, MethodParameters, EnclosingMethod, InnerClasses

# Preserve Serializable classes
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    private void readObjectNoData();
}

# Preserve all members of classes in the kotlinx.coroutines package
# (This is necessary for Kotlin coroutine functionality)
-keepclassmembers class kotlinx.coroutines.** { *; }

# Suppress warnings related to kotlinx.coroutines
-dontwarn kotlinx.coroutines.**

# --- Specific ProGuard rules for feat_nfc classes ---

# Keep HiFCMManager and its public members
-keep class com.mwkg.fcm.HiFCMManager { *; }

# ----------------------------------------
# Firebase and Google Play Services
# ----------------------------------------

# Preserve the Firebase Messaging Service implementation
# (Required for receiving messages from FCM)
-keep class com.google.firebase.messaging.FirebaseMessagingService { *; }

# Preserve all classes in the Firebase Instance ID package
# (Necessary for Firebase token generation and management)
-keep class com.google.firebase.iid.** { *; }
# Suppress warnings related to Firebase Instance ID
-dontwarn com.google.firebase.iid.**

# Preserve all classes in the Firebase package
# (This ensures smooth operation of Firebase libraries)
-keep class com.google.firebase.** { *; }
# Suppress warnings related to Firebase
-dontwarn com.google.firebase.**

# Preserve all classes in the Google Play Services package
# (Required for integration with Google services like Maps or Authentication)
-keep class com.google.android.gms.** { *; }
# Suppress warnings related to Google Play Services
-dontwarn com.google.android.gms.**

# ----------------------------------------
# Log Removal
# ----------------------------------------

# Remove log statements (e.g., Log.v, Log.d) in release builds
# (This reduces APK size and removes potentially sensitive information)
-assumenosideeffects class android.util.Log {
    public static *** v(...);
    public static *** d(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}

# ----------------------------------------
# R8 Warnings
# ----------------------------------------

# Suppress warnings related to the StringConcatFactory class
# (This warning occurs in Java 9 and above during string concatenation optimizations)
-dontwarn java.lang.invoke.StringConcatFactory

# ----------------------------------------
# Gson
# ----------------------------------------

# Preserve all classes in the Gson package
# (Required if Gson is used for JSON parsing in the app)
-keep class com.google.gson.** { *; }
# Suppress warnings related to Gson
-dontwarn com.google.gson.**

# ----------------------------------------
# Android Base Classes
# ----------------------------------------

# Preserve all Android framework classes
# (Ensures the Android framework is not obfuscated)
-keep class android.** { *; }
# Suppress warnings related to Android classes
-dontwarn android.**