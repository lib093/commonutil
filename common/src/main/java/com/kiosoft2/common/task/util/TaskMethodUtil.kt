package com.kiosoft2.common.task.util

import com.kiosoft2.common.task.annotions.TaskBindDisposable
import com.kiosoft2.common.task.annotions.TaskComplete
import com.kiosoft2.common.task.interfaces.TaskReLoadCallback
import com.kiosoft2.common.task.model.TaskInfo

object TaskMethodUtil {
    fun invokeMethod(o:Any,annotationClass: Class<out Annotation>,taskInfo: TaskInfo){
        val methods = o.javaClass.methods
        for (method in methods) {
            //方法是否被annotation所注解
//            val annotationPresent = method.isAnnotationPresent(annotationClass)
            val annotation = method.getAnnotation(annotationClass)
            if (annotation != null){
//                val annotation = method.getAnnotation(annotationClass)
                if (annotationClass == TaskBindDisposable::class.java){
                    method.invoke(o,taskInfo)
                }else if (annotationClass == TaskComplete::class.java){
                    method.invoke(o,taskInfo)
                }
            }
        }
    }

    fun invokeMethodTaskReLoad(o:Any,taskInfo: TaskInfo){
        val method = o.javaClass.getMethod("onReLoadStart",TaskInfo::class.java)
        method.invoke(o,taskInfo)
    }
}