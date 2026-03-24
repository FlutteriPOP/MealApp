# Retrofit rules
-keepattributes Signature, InnerClasses, AnnotationDefault
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepattributes RuntimeInvisibleAnnotations, RuntimeInvisibleParameterAnnotations
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keep @interface retrofit2.http.** { *; }

# Gson rules
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keep class com.google.gson.reflect.TypeToken
-keep class * extends com.google.gson.TypeAdapter

# Keep Data Models (Gson needs them for reflection)
-keep class com.dev.mealapp.data.model.** { *; }

# Coil rules
-keep class coil.** { *; }
-dontwarn coil.**

# YouTube Player rules
-keep class com.pierfrancescosoffritti.androidyoutubeplayer.** { *; }
-dontwarn com.pierfrancescosoffritti.androidyoutubeplayer.**

# Kotlin Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.coroutines.android.HandlerContext$Main {
    private <init>(...);
}
