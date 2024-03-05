package com.kiosoft2.processor.config

object AProcessorConfig {
    //注解全类名
    const val APARAMETER = "com.kiosoft2.annotation.AParameter"
    const val OPTIONS = "moduleName"//modle名称
    const val APT_PACKAGE = "packageNameForAPT"//APT存放的包名
    const val APT_PACKAGE_DEFULT = "com.kiosoft2.apt"//APT存放的包名
    const val ACTIVITY_PACKAGE = "android.app.Activity"
    const val Fragment_PACKAGE = "androidx.fragment.app.Fragment"
    const val API_PACKAGE = "com.kiosoft2.api.aparameter.api"
    const val METHOD_APARAMETER_GET = "getParameter"
    const val APARAMETERGET = "AParameterGet"

    // ARouter api 的 ParameterGet 方法参数的名字
    var PARAMETER_NAME = "targetParameter"
    const val STRING = "java.lang.String"
    const val SERIALIZABLE = "java.io.Serializable"
    const val BUNDLE = "android.os.Bundle"
    const val LIST_BUNDLE = "java.util.List<android.os.Bundle>"

    var PARAMETE_FILE_NAME = "AParameter$$"

}