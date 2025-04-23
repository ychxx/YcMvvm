# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class androidx.annotation.Keep { <init>(...); }

# Keep glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * implements com.bumptech.glide.module.GlideModule { <init>(...); }

# Keep retrofit2
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep,allowshrinking class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-keep class * implements okhttp3.Interceptor { *; }


# Keep XLog
-keep class com.elvishew.xlog.** { *; }

# Keep SmartRefreshLayout components
-keep class com.scwang.smart.refresh.** { *; }
-keep class com.scwang.smart.refresh.layout.** { *; }

# Keep xUtils
-keep class org.xutils.** { *; }
-keepclassmembers class * extends org.xutils.** { *; }

# Keep ycmvvm
-keep class com.yc.ycmvvm.**{*;}
-keepclassmembers class com.yc.ycmvvm.**$* { *; }
-keepclasseswithmembers class * {
    @com.yc.ycmvvm.annotation.* <fields>;
}

-dontwarn java.lang.invoke.StringConcatFactory