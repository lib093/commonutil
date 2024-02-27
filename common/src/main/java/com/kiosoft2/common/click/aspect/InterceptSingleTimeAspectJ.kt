package com.kiosoft2.common.click.aspect

import android.annotation.SuppressLint
import android.util.Log
import android.util.LruCache
import com.kiosoft2.common.click.annotions.SingleTime
import com.kiosoft2.common.task.aspect.TaskAspectJ
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import java.lang.reflect.Method


@Aspect
class InterceptSingleTimeAspectJ {
    companion object{
        var lruCache = LruCache<String, Long>(100)
        @JvmStatic
        fun aspectOf(): InterceptSingleTimeAspectJ {
            return InterceptSingleTimeAspectJ()
        }
    }
    @Pointcut("execution(@com.kiosoft2.common.click.annotions.SingleTime * *(..))" + " && @annotation(singleTime)")
    fun requestSingleTimeMethod(singleTime: SingleTime) {
    }

    @Around("requestSingleTimeMethod(singleTime)")
    fun aroundJoinPoint(joinPoint: ProceedingJoinPoint, singleTime: SingleTime){
        if (singleTime != null) {
            val time = singleTime.time
            getMethod(joinPoint)?.name?.let{
              var lastTime = lruCache.get(it)?:0
                if (System.currentTimeMillis() - lastTime > time) {
                    lruCache.put(it, System.currentTimeMillis())
                    joinPoint.proceed()
                }
            }
        }else{
             joinPoint.proceed()
        }
    }
    fun getMethod(joinPoint: ProceedingJoinPoint): Method? {
        val signature = joinPoint.signature
        return if (signature is MethodSignature) {
            signature.method
        } else {
            null
        }
    }
}