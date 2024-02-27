package com.kiosoft2.common.thead.aspect

import android.util.Log
import com.kiosoft2.common.click.aspect.InterceptSingleTimeAspectJ
import com.kiosoft2.common.thead.annotions.RunInIO
import com.kiosoft2.common.thead.utils.AppExecutors
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut

/**
 * 线程切换 切换至主线程
 */
@Aspect
class InterceptIOThreadAspectJ {
    companion object{
        @JvmStatic
        fun aspectOf(): InterceptIOThreadAspectJ {
            return InterceptIOThreadAspectJ()
        }
    }
    @Pointcut("execution(@com.kiosoft2.common.thead.annotions.RunInIO * *(..))" + " && @annotation(runInIO)")
    fun requestRunInIOMethod(runInIO: RunInIO) {
    }

    @Around("requestRunInIOMethod(runInIO)")
    fun aroundJoinPoint(joinPoint: ProceedingJoinPoint, runInIO: RunInIO){
        if (runInIO != null) {
            AppExecutors.instance.networkIO.execute { joinPoint.proceed() }
        }else{
            joinPoint.proceed()
        }
    }
}