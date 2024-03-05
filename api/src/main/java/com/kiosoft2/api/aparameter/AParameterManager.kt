package com.kiosoft2.api.aparameter

import android.app.Activity
import android.app.Fragment
import android.util.Log
import android.util.LruCache
import com.kiosoft2.api.aparameter.api.AParameterGet
import org.w3c.dom.DocumentFragment
import kotlin.math.log

class AParameterManager private constructor(){
    val PARAMETE_FILE_NAME = "AParameter$$"
    val APT_PACKAGE_DEFULT = "com.kiosoft2.apt"//APT存放的包名
    val parameterCache: LruCache<String, AParameterGet> = LruCache(100)
    companion object{
        val instance: AParameterManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            AParameterManager() }
    }
    fun load(obj:Any){
        var className = obj::class.simpleName
        var parameterLoad = parameterCache[className]
        try {
            if (null == parameterLoad){
                //获取当前页面对应的辅助类
                var aClass = Class.forName(APT_PACKAGE_DEFULT+"."+PARAMETE_FILE_NAME+className)
                parameterLoad = aClass.newInstance() as AParameterGet
                parameterCache.put(className,parameterLoad)
            }
            parameterLoad.getParameter(obj)
        }catch (e:Exception){
            Log.e("lance", e.message.toString() )
        }

    }

}