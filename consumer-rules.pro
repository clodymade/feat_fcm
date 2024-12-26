# Keep all public classes and methods in the feat_fcm package
-keep class com.mwkg.fcm.** { *; }

# Keep specific classes and their public members (if applicable)
-keep class com.mwkg.fcm.HiFCMManager { *; }

# Keep all annotations in the library
-keepattributes *Annotation*

# Keep the method parameters and signatures
-keepattributes Signature, MethodParameters

# Preserve Serializable classes
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    private void readObjectNoData();
}