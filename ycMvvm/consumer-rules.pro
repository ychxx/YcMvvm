-keep class androidx.annotation.Keep { <init>(...); }
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * implements com.bumptech.glide.module.GlideModule { <init>(...); }


-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep,allowshrinking class okhttp3.internal.publicsuffix.PublicSuffixDatabase

-keep class com.yc.ycmvvm.**{*;}
-keep class org.xutils.**{*;}