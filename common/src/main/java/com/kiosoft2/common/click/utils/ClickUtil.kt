package com.kiosoft2.common.click.utils

/**
 * 防抖工具类
 */
object  ClickUtil {
    var lastClickTime:Long = 0
    var click_interval_time:Long = 800 //防抖间隔时常
    fun isFastDoubleClick():Boolean{
        var flag = false
        val currentClickTime = System.currentTimeMillis()
        if (currentClickTime - lastClickTime < click_interval_time){
            flag = true
        }
        if (!flag)
            lastClickTime = currentClickTime
        return flag
    }
}