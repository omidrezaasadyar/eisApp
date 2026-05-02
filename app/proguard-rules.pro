# Hilt
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.android.lifecycle.HiltViewModel
-keepclasseswithmembernames class * { @dagger.hilt.android.lifecycle.HiltViewModel <init>(...); }

# Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keep,includedescriptorclasses class com.eis.oman.**$$serializer { *; }
-keepclassmembers class com.eis.oman.** {
    *** Companion;
}
-keepclasseswithmembers class com.eis.oman.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Firebase / Firestore (DTOs use reflection)
-keep class com.eis.oman.data.dto.** { *; }
-keepclassmembers class com.eis.oman.data.dto.** {
    <init>();
    <init>(...);
    public *;
}

# Lottie
-keep class com.airbnb.lottie.** { *; }
-dontwarn com.airbnb.lottie.**

# Coroutines (debug agent stripping)
-dontwarn kotlinx.coroutines.debug.**

# Compose
-dontwarn androidx.compose.**
