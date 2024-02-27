package com.kiosoft2.common.task.aspect

import android.util.Log
import com.google.gson.Gson
import com.kiosoft2.common.task.annotions.RecurringTask
import com.kiosoft2.common.task.annotions.TaskThread
import com.kiosoft2.common.task.helper.RecurringTaskHelper
import com.kiosoft2.common.task.interfaces.JoinPointRunCallback
import com.kiosoft2.common.task.model.MethodInfo
import com.kiosoft2.common.task.model.ParameterInfo
import com.kiosoft2.common.task.model.TaskInfo
import com.kiosoft2.common.task.util.TaskManager
import com.kiosoft2.common.task.util.TimeUtil
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.Signature
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import java.util.UUID


/**
 * 轮询任务
 */
@Aspect
class TaskAspectJ {
    companion object {
        @JvmStatic
        fun aspectOf(): TaskAspectJ {
            return TaskAspectJ()
        }
    }

    @Pointcut("execution(@com.kiosoft2.common.task.annotions.RecurringTask * *(..))" + " && @annotation(recurringTask)")
    fun requestTaskMethod(recurringTask: RecurringTask) {
    }

    @Around("requestTaskMethod(recurringTask)")
    fun aroundJoinPoint(joinPoint: ProceedingJoinPoint, recurringTask: RecurringTask) {
        Log.d("lance", "requestTaskMethod ：${Thread.currentThread().name}")
        if (recurringTask != null) {
            var delayTime: Long =
                TimeUtil.conventTimeMillis(recurringTask.time, recurringTask.unit)
            var endTimeMillis = System.currentTimeMillis() + delayTime
            val taskInfo = TaskInfo(
                owerClassName = joinPoint.`this`.javaClass.name,//ownerClassName 任务所属类名
                annotationClass = RecurringTask::class.java,
                annotationClassName = RecurringTask::class.java.name,
                lifecycleOwner = null,
                taskId = UUID.randomUUID().toString(),//taskId 任务id
                taskCode = recurringTask.taskCode,//taskCode 任务code
                endTimeMillis = endTimeMillis,//endTimeMillis 任务结束时间时间戳
                threadType = recurringTask.threadType,
                initialDelay = recurringTask.initialDelay, //初始延迟时间
                period = recurringTask.period, //任务执行间隔时间
                time = recurringTask.time, //任务执行时长
                unit = recurringTask.unit, //时间单位
                isRepeat = recurringTask.isRepeat,
                isReStart = recurringTask.isReStart,
                isCacheResumeStart = recurringTask.isCacheResumeStart
            )
            if (!recurringTask.isRepeat && TaskManager.isExistTask(
                    joinPoint.`this`,
                    recurringTask.taskCode,
                    RecurringTask::class.java
                )
            ) {
                // 不重复  已存在任务
                if (recurringTask.isReStart) {
                    //需要重新启动
                    taskInfo.isReStart = recurringTask.isReStart
                    //需要重新启动,先关闭再启动新的
                    TaskManager.removeTask(joinPoint.`this`, taskInfo)
                } else {
                    //不需要重新启动 不执行
                    Log.d(
                        "lance",
                        "当前任务已存在正在执行的任务，  RecurringTask  ${recurringTask.taskCode}  不再执行"
                    )
                    return
                }
            }
            Log.d(
                "lance",
                " ${joinPoint.`this`.javaClass.name}  RecurringTask  ${taskInfo.taskId}   ${recurringTask.taskCode}  创建新任务"
            )
            RecurringTaskHelper().start(joinPoint.`this`, recurringTask, taskInfo, object :
                JoinPointRunCallback {
                override fun run() {
                    joinPoint.proceed()
                }
            })
        } else {
            joinPoint.proceed()
        }
    }

    fun getMethodParameterInfo(joinPoint: ProceedingJoinPoint): MutableList<Class<*>> {
        // 在方法执行前获取方法签名信息
        val signature: Signature = joinPoint.getSignature()
        // 获取方法的参数类型
        val parameterTypes = (signature as MethodSignature).parameterTypes
        var parameterInfoList = mutableListOf<Class<*>>()

        // 输出方法的参数类型
        for (parameterType in parameterTypes) {
            parameterInfoList.add(parameterType)
        }
        return parameterInfoList
    }
}


fun Observable<*>.threadSwitch(): Observable<*> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun Observable<*>.threadType(taskThread: TaskThread): Observable<*> {
    if (taskThread == TaskThread.MAIN_THREAD) {
        return this.observeOn(AndroidSchedulers.mainThread())
    } else if (taskThread == TaskThread.IO_THREAD) {
        return this.observeOn(Schedulers.io())
    } else {
        //当前线程
        return this
    }
}

