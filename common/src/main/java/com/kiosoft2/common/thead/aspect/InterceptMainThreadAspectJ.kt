package com.kiosoft2.common.thead.aspect

import android.util.Log
import com.kiosoft2.common.thead.annotions.RunInMainThread
import com.kiosoft2.common.thead.utils.AppExecutors
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut

/**
 * 线程切换 切换至主线程
 */
@Aspect
class InterceptMainThreadAspectJ {
    companion object{
        @JvmStatic
        fun aspectOf(): InterceptMainThreadAspectJ {
            return InterceptMainThreadAspectJ()
        }
    }
    @Pointcut("execution(@com.kiosoft2.common.thead.annotions.RunInMainThread * *(..))" + " && @annotation(runInMainThread)")
    fun requestRunInMianMethod(runInMainThread: RunInMainThread) {
    }

    @Around("requestRunInMianMethod(runInMainThread)")
    fun aroundJoinPoint(joinPoint: ProceedingJoinPoint, runInMainThread: RunInMainThread){
        if (runInMainThread != null) {
            AppExecutors.instance.mainThread.execute { joinPoint.proceed() }
        }else{
            joinPoint.proceed()
        }
    }
}