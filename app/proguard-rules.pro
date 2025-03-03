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


###################################################################
#                                                                 #
#                          CONFIGURATION                          #
#                                                                 #
###################################################################

-printseeds seeds.txt
-printusage unused.txt
-printmapping mapping.txt

###################################################################
#                                                                 #
#                          OPTIMIZATIONS                          #
#                                                                 #
###################################################################

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-optimizations !class/unboxing/enum
-optimizationpasses 5
-allowaccessmodification
-dontpreverify

# See http://www.crashlytics.com/blog/mastering-proguard-for-building-lightweight-android-code/ for the following config options
-keepattributes SourceFile,LineNumberTable
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose


###################################################################
#                                                                 #
#            Dontwarn the compile gradle jar libs                 #
#                                                                 #
###################################################################
-dontwarn org.apache.commons.**
-dontwarn org.apache.http.**
-dontwarn com.google.maps.android.**
-dontwarn com.google.android.gms.**
-dontwarn com.squareup.picasso.**
-dontwarn android.os.**

-dontwarn okio.**
-dontwarn retrofit2.Platform$Java8
-dontwarn java.awt.**,javax.swing.**
-dontwarn java.awt.**,javax.swing.**
-dontwarn com.google.common.cache.**
-dontwarn com.google.common.primitives.**
-dontwarn android.security.NetworkSecurityPolicy
-dontwarn java.nio.file.Files
-dontwarn java.nio.file.Path
-dontwarn java.nio.file.OpenOption
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn org.joda.time.**
-dontwarn org.joda.convert.FromString
-dontwarn org.joda.convert.ToString
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
-dontwarn sun.misc.Unsafe
# Needed by google-http-client-android when linking against an older platform version
-dontwarn com.google.api.client.extensions.android.**

# Needed by google-api-client-android when linking against an older platform version

-dontwarn com.google.api.client.googleapis.extensions.android.**
#-dontwarn okio.**
#-dontwarn retrofit2.Platform$Java8


###################################################################
#                                                                 #
#            To Remove Log and print messages                      #
#                                                                 #
###################################################################

-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}


###################################################################
#                                                                 #
#            Keep Attributes and annotations                      #
#                                                                 #
###################################################################

-keepattributes EnclosingMethod, InnerClasses
-keepattributes *Annotation*
-keepattributes Signature



###################################################################
#                                                                 #
#                   Keep Classes for proguard                     #
#                                                                 #
###################################################################

#-keep class * extends android.app.AppCompatActivity
#-keep public class * extends android.app.Application
#-keep public class * extends android.content.BroadcastReceiver


-dontwarn com.squareup.okhttp.**

-dontwarn rx.**
-dontwarn retrofit.**
-keep class retrofit.** { *; }

-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *; }
-keep interface com.squareup.okhttp3.** { *; }
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }

-keep class org.apache.http.** { *; }
-keep class com.android.support.** { *; }
-keep class com.google.android.gms.** { *; }
-keep class com.google.maps.android.** { *; }
-keep class com.squareup.picasso.** { *; }
# CRASHLYTICS
-keep class com.crashlytics.** { *; }

# JODATIME
-keep public class org.joda.time.** {public private protected *;}

# ANDROID SUPPORT LIBRARIES
# support-v4
#-dontwarn android.support.v4.**
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }

# support-v7
#-dontwarn android.support.v7.**
-keep class android.support.v7.internal.** { *; }
-keep interface android.support.v7.internal.** { *; }
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }

# GOOGLE PLAY SERVICES
-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keepnames class com.realappraiser.gharvalue.AppDatabase{ *; }
-keep class com.realappraiser.gharvalue.model.*{ *; }
-keep class com.realappraiser.gharvalue.communicator.*{ *; }


# GOOGLE API CLIENT LIBRARIES
# Needed to keep generic types and @Key annotations accessed via reflection
-keepattributes Signature,RuntimeVisibleAnnotations,AnnotationDefault

-keepclassmembers class * {
  @com.google.api.client.util.Key <fields>;
}

-dontpreverify
-dontwarn com.faendir.rhino.**
-dontwarn org.mozilla.javascript.**
-dontwarn javax.script.ScriptEngineManager.**
-dontwarn com.squareup.okhttp.**
# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer


# Retain generated class which implement Unbinder.
-keep public class * implements butterknife.Unbinder { public <init>(**, android.view.View); }

# Prevent obfuscation of types which use ButterKnife annotations since the simple name
# is used to reflectively look up the generated ViewBinding.
-keep class butterknife.*
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

-keep class org.mozilla.classfile.** { *; }
-keep class org.mozilla.javascript.* { *; }
-keep class org.mozilla.javascript.annotations.** { *; }
-keep class org.mozilla.javascript.ast.** { *; }
-keep class org.mozilla.javascript.commonjs.module.** { *; }
-keep class org.mozilla.javascript.commonjs.module.provider.** { *; }
-keep class org.mozilla.javascript.debug.** { *; }
-keep class org.mozilla.javascript.jdk13.** { *; }
-keep class org.mozilla.javascript.jdk15.** { *; }
-keep class org.mozilla.javascript.json.** { *; }
-keep class org.mozilla.javascript.optimizer.** { *; }
-keep class org.mozilla.javascript.regexp.** { *; }
-keep class org.mozilla.javascript.serialize.** { *; }
-keep class org.mozilla.javascript.typedarrays.** { *; }
-keep class org.mozilla.javascript.v8dtoa.** { *; }
-keep class org.mozilla.javascript.xml.** { *; }
-keep class org.mozilla.javascript.xmlimpl.** { *; }
-keep class org.mozilla.javascript.tools.**



-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int d(...);
    public static int i(...);
}

