package com.kiosoft2.common.task.aspect

import android.util.Log
import com.kiosoft2.common.task.annotions.DelayedTask
import com.kiosoft2.common.task.helper.DelayedTaskHelper
import com.kiosoft2.common.task.interfaces.JoinPointRunCallback
import com.kiosoft2.common.task.model.TaskInfo
import com.kiosoft2.common.task.util.TaskManager
import com.kiosoft2.common.task.util.TimeUtil
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import java.util.UUID

/**
 * 轮询任务
 */
@Aspect
class DelayedTaskAspectJ {
    companion object {
        @JvmStatic
        fun aspectOf(): DelayedTaskAspectJ? {
            return DelayedTaskAspectJ()
        }
    }

    @Pointcut("execution(@com.kiosoft2.common.task.annotions.DelayedTask * *(..))" + " && @annotation(delayedTask)")
    fun requestDelayedTaskMethod(delayedTask: DelayedTask) {
    }

    @Around("requestDelayedTaskMethod(delayedTask)")
    fun aroundJoinPoint(joinPoint: ProceedingJoinPoint, delayedTask: DelayedTask) {
        Log.d("lance", "requestDelayedTaskMethod: ")
        if (delayedTask != null) {
            val ownerClassName = joinPoint.`this`.javaClass.name
            var delayTime: Long =
                TimeUtil.conventTimeMillis(delayedTask.initialDelay, delayedTask.unit)
            var endTimeMillis = System.currentTimeMillis() + delayTime
            val taskInfo = TaskInfo(
                owerClassName = ownerClassName,//ownerClassName 任务所属类名
                annotationClass = DelayedTask::class.java,
                annotationClassName = DelayedTask::class.java.name,
                lifecycleOwner = null,
                taskId = UUID.randomUUID().toString(),//taskId 任务id
                taskCode = delayedTask.taskCode,//taskCode 任务code
                endTimeMillis = endTimeMillis,//endTimeMillis 任务结束时间时间戳
                threadType = delayedTask.threadType
            )
            if (!delayedTask.isRepeat && TaskManager.isExistTask(joinPoint.`this`, delayedTask.taskCode,DelayedTask::class.java)) {
                if (delayedTask.isReStart) {
                    //需要重新启动
                    TaskManager.removeTask(joinPoint.`this`,  taskInfo)
                } else {
                    Log.d("lance", "当前任务已存在正在执行的任务，   wDelayedTask    ${delayedTask.taskCode} 不再执行")
                    return
                }
                Log.d("lance", "当前任务已存在正在执行的任务，    DelayedTask    ${delayedTask.taskCode} 不再执行")
                return
            }
            Log.d(
                "lance",
                " ${taskInfo.owerClassName}  DelayedTask ${taskInfo.taskId}   ${delayedTask.taskCode}  创建新任务"
            )
           DelayedTaskHelper().start(joinPoint, delayedTask, taskInfo,object :JoinPointRunCallback{
               override fun run() {
                   joinPoint.proceed()
               }
           })
        } else {
            joinPoint.proceed()
        }
    }
}




