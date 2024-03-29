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
