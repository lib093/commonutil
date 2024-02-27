package com.kiosoft2.common.click.aspect

import android.util.Log
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import java.lang.reflect.Method
//必须
@Aspect
class InterceptClickAspectJ {
    // 最后一次点击的时间
    private var lastTime = 0L

    //正常点击事件
//    @Around("execution(* android.view.View.OnClickListener.onClick(..))")
//    @Throws(Throwable::class)
//    fun clickIntercept(joinPoint: ProceedingJoinPoint) {
//        try {
//            val method: Method? = getMethod(joinPoint)
//            if (method == null) {
//                Log.d("lance", "method == null")
//                if (!isFastDoubleClick()) {
//                    Log.d("lance", "执行点击事件")
//                    joinPoint.proceed()
//                } else {
//                    Log.d("lance", "刚点击过不在执行")
//                }
//                return
//            }
//            val annotations = method.annotations
//            var isFilter = true
//            if (annotations != null && annotations.size > 0) {
//                for (index in annotations.indices) {
//                    val annotation = annotations[index]
//                    if (annotation is ClickFilter) {
//                        isFilter = false
//                        break
//                    }
//                }
//                if (isFilter) {
//                    if (!isFastDoubleClick()) {
//                        Log.d("lance", "执行点击事件")
//                        joinPoint.proceed()
//                    } else {
//                        Log.d("lance", "刚点击过不在执行")
//                    }
//                } else {
//                    Log.d("lance", "不拦截直接执行点击事件")
//                    joinPoint.proceed()
//                }
//            } else {
//                if (!isFastDoubleClick()) {
//                    Log.d("lance", "执行点击事件")
//                    joinPoint.proceed()
//                } else {
//                    Log.d("lance", "刚点击过不在执行")
//                }
//            }
//        } catch (e: Throwable) {
//            throw RuntimeException(e)
//        }
//    }
//
//    //使用lambda的点击事件
//    @Around("execution(* *..*lambda*(..))")
//    @Throws(Throwable::class)
//    fun clickInterceptLambda(joinPoint: ProceedingJoinPoint)  {
//        try {
//            Log.d("lib93499999999", "joinPoint.signature != null  ${joinPoint.signature.declaringType}")
//            Log.d("lib93499999999", "joinPoint.kind != null  ${joinPoint.kind}")
//            Log.d("lib93499999999", "joinPoint.signature.name != null  ${joinPoint.signature.name}")
//            Log.d("lib93499999999", "joinPoint.signature.declaringTypeName != null  ${joinPoint.signature.declaringTypeName}")
//            Log.d("lib93499999999", "joinPoint.args != null  ${joinPoint.args}")
//            val method: Method? = getMethod(joinPoint)
//            if (method == null) {
//                Log.d("lance", "method == null")
//                if (!isFastDoubleClick()) {
//                    Log.d("lance", "执行点击事件")
//                    joinPoint.proceed()
//                } else {
//                    Log.d("lance", "刚点击过不在执行")
//                }
//                return
//            }else{
//                Log.d("lance", "method !!!!!!!= null")
//            }
//            val annotations = method.annotations
//            var isFilter = true
//            if (annotations != null && annotations.size > 0) {
//                for (index in annotations.indices) {
//                    val annotation = annotations[index]
//                    if (annotation is ClickFilter) {
//                        isFilter = false
//                        break
//                    }
//                }
//                if (isFilter) {
//                    if (!isFastDoubleClick()) {
//                        Log.d("lance", "执行点击事件")
//                        joinPoint.proceed()
//                    } else {
//                        Log.d("lance", "刚点击过不在执行")
//                    }
//                } else {
//                    Log.d("lance", "不拦截直接执行点击事件")
//                    joinPoint.proceed()
//                }
//            } else {
//                if (!isFastDoubleClick()) {
//                    Log.d("lance", "执行点击事件")
//                    joinPoint.proceed()
//                } else {
//                    Log.d("lance", "刚点击过不在执行")
//                }
//            }
//        } catch (e: Throwable) {
//            throw RuntimeException(e)
//        }
//    }
     fun getMethod(joinPoint: ProceedingJoinPoint): Method? {
        val signature = joinPoint.signature
        return if (signature is MethodSignature) {
            signature.method
        } else {
            null
        }
    }
}