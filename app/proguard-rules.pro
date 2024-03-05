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
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-dump class_files.txt
-printseeds seeds.txt
-printusage unused.txt
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-ignorewarnings

-allowaccessmodification
-keepattributes *Annotation*
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
-repackageclasses ''
#-printmapping mapping.txt

-keep public class * extends java.lang.Exception
 #保持自定义库的所有类和成员不被混淆
-keep class com.kiosoft2.common.** { *; }
-keep class com.kiosoft2.common.**.** { *; }
-keep class com.kiosoft2.processor.** { *; }
-keep class com.kiosoft2.api.**.** { *; }
-keep class com.kiosoft2.api.** { *; }
-keep class com.squareup.javapoet.** { *; }
# 如果自定义库中有一些特定的注解需要保持不被混淆，可以添加类似的规则
-keep @com.kiosoft2.common.thead.annotions.** class * {*;}
-keep @com.kiosoft2.common.click.annotions.** class * {*;}
-keep @com.kiosoft2.common.task.annotions.** class * {*;}

# 如果自定义库中使用了一些特定的接口，也需要保持不被混淆
-keep interface com.example.customlibrary.interfaces.** { *; }
-keep interface com.kiosoft2.common.task.interfaces.** { *; }

# 如果自定义库包含一些特定的枚举类型，同样需要保持不被混淆
-keepclassmembers enum com.kiosoft2.common.task.annotions.** { *; }


-keep class com.kiosoft2.common.autoservice.aspect.TaskServiceBindAspect {*;}
-keepclassmembers class * {
    @com.kiosoft2.common.autoservice.annotions.BindTaskService *;
}


# Room
-keep class androidx.room.paging.** {
    *;
}
-keep class androidx.room.** {
    *;
}
# Room database entities should be excluded from obfuscation
-keep @androidx.room.Entity class * {
    *;
}

# Keep the default constructor for Room database entities
-keepclassmembers class * {
    @androidx.room.Entity <init>(...);
}

-keep class org.jetbrains.kotlinx.coroutines.** { *; }
-keep class kotlinx.coroutines.android.** {*;}


# 不混淆 TaskServiceImpl 类名
-keepnames class com.kiosoft2.encrypt.EncryptServiceImpl

# 不混淆 TaskServiceImpl 类的方法名
-keepclassmembers  class com.kiosoft2.encrypt.EncryptServiceImpl {
    public <methods>;
}
#不混淆apt产生的辅助类
-keep class com.kiosoft2.apt.** { *; }
-keep class com.kiosoft2.apt.**.** { *; }
-keep @com.kiosoft2.annotions.** class * {*;}