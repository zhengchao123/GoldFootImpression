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

-optimizationpasses 5
-ignorewarnings
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

-keep   class com.amap.api.services.**{*;}

-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

-keep class com.baidu.** {*;}

-keep class vi.com.** {*;}
-dontwarn com.baidu.**
-keep class sun.misc. {*;}
-dontwarn sun.misc.****

#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}

#okio
-dontwarn okio.**
-keep class okio.**{*;}
-keepattributes JavascriptInterface
-keepclassmembers class * {
@android.webkit.JavascriptInterface <methods>;
}
-keep class com.baidu.** {*;}
-keep class mapsdkvi.com.** {*;}
-keep class vi.com.gdi.bgl.android.**{*;}
-keep class android.databinding.** { *; }
-keep  class com.gold.footimpression.module.** { *; }
-keepattributes Signature

-dontwarn com.baidu.**
-dontwarn vi.com.**
-dontwarn pvi.com.**
-dontshrink
-verbose
-keepattributes Signature

-dontskipnonpubliclibraryclasses



-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *; }
-keepnames class * implements java.io.Serializable
#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}
# For using GSON @Expose annotation
-keepattributes *Annotation*
#-libraryjars libs/passguard.jar
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-ignorewarning

-dontwarn android.databinding.**
#
-keep class cn.passguard.** { *; }
-keep class org.spongycastle.** { *; }
-keep class com.google.gson.** { *; }
-keep class com.google.gson.stream.** { *; }
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }
-keep class com.google.protobuf.** {*;}

-dontwarn com.igexin.**
-keep class com.igexin.** { *; }
-keep class org.json.** { *; }

-keep public class * implements java.io.Serializable {*;}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#-keep public class com.rcb.financialservice.model.BaseNetObjectModule { *; }
#-keep public class com.rcb.financialservice.model.AcountModule { *; }
#-keep public class com.rcb.financialservice.model.BaseNetArrayModule { *; }
#-keep public class com.rcb.financialservice.model.BaseNetStringModule { *; }
#-keep public class com.rcb.financialservice.model.CreateServiceResultModule { *; }
#-keep public class com.rcb.financialservice.model.CustomerSourceModule { *; }
#-keep public class com.rcb.financialservice.model.EditServiceModule { *; }
#-keep public class com.rcb.financialservice.model.OrderDetailModule { *; }
#
#-keep public class com.rcb.financialservice.model.OrderIncrementModule { *; }
#-keep public class com.rcb.financialservice.model.OrderModule { *; }
#-keep public class com.rcb.financialservice.model.PersonModule { *; }
#-keep public class com.rcb.financialservice.model.PlannerModule { *; }
#-keep public class com.rcb.financialservice.model.PlannerStateModule { *; }
#-keep public class com.rcb.financialservice.model.ReiceverModule { *; }
#-keep public class com.rcb.financialservice.model.RoomAndCardModule { *; }
#-keep public class com.rcb.financialservice.model.RoomStateModule { *; }
#
#-keep public class com.rcb.financialservice.model.ServiceIncrementModule { *; }
#-keep public class com.rcb.financialservice.model.ServiceItemModule { *; }
#-keep public class com.rcb.financialservice.model.TimeModule { *; }
#-keep public class com.rcb.financialservice.model.VIPInfoModule { *; }
#-keep public class com.rcb.financialservice.model.BaseNetPwdModule { *; }


