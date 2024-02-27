package com.kiosoft2.common.task.util

import android.util.Log
import com.kiosoft2.common.task.annotions.DelayedTask
import com.kiosoft2.common.task.annotions.RecurringTask
import com.kiosoft2.common.task.helper.DelayedTaskHelper
import com.kiosoft2.common.task.helper.RecurringTaskHelper
import com.kiosoft2.common.task.interfaces.JoinPointRunCallback
import com.kiosoft2.common.task.interfaces.TaskReLoadCallback
import com.kiosoft2.common.task.model.TaskInfo

object TaskReLoadUtil {
    fun reLoadStart(obj: Any,
               taskInfo: TaskInfo,
              callBack: TaskReLoadCallback
    ){
        Log.d("lance", "缓存任务开始重新加载执行，  ${taskInfo.annotationClassName} ${taskInfo.taskId}  ${taskInfo.taskCode}  重新加载执行")
        if (taskInfo.annotationClassName == RecurringTask::class.java.name){
            taskInfo.annotationClass = RecurringTask::class.java
            var recurringTask = RecurringTask(taskInfo.taskCode, taskInfo.threadType, taskInfo.initialDelay, taskInfo.period, taskInfo.time, taskInfo.unit, taskInfo.isRepeat, taskInfo.isReStart,taskInfo.isCacheResumeStart)
            RecurringTaskHelper().start(obj, recurringTask,  taskInfo,object :
                JoinPointRunCallback {
                override fun run() {
                    callBack.onReLoadStart(taskInfo)
                }
            })
        }else if (taskInfo.annotationClassName == DelayedTask::class.java.name){
            taskInfo.annotationClass = DelayedTask::class.java
            var delayedTask = DelayedTask(taskCode = taskInfo.taskCode, initialDelay = taskInfo.initialDelay,taskInfo.threadType, taskInfo.unit, taskInfo.isRepeat, taskInfo.isReStart,taskInfo.isCacheResumeStart)
            DelayedTaskHelper().start(obj, delayedTask,  taskInfo,object :
                JoinPointRunCallback {
                override fun run() {
                    callBack.onReLoadStart(taskInfo)
                }
            })
        }


    }
}