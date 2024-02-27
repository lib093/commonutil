package com.kiosoft2.common.autoservice.aspect

import org.aspectj.lang.annotation.Aspect


@Aspect
class TaskServiceBindAspect {
    companion object{
        @JvmStatic
        fun aspectOf(): TaskServiceBindAspect {
            return TaskServiceBindAspect()
        }
    }
//    @Before("get(* @com.kiosoft2.common.autoservice.annotions.BindTaskService * *) && target(obj)")
//    fun autoInstantiate(obj: Any){
//        Log.d("lib9340424355555", "autoInstantiate: ")
//        val clazz: Class<*> = obj.javaClass
//
//        for (field in clazz.declaredFields) {
//            if (field.isAnnotationPresent(BindTaskService::class.java)) {
//                field.isAccessible = true
//                Log.d("lib9340424355555", "autoInstantiate: 555555555555555555555555555555555555")
//                // 在这里进行自动赋值的操作
//                // 例如，实例化服务并将其赋值给属性
//                field.set(obj, TaskServiceImpl())
//            }
//        }
//    }
}