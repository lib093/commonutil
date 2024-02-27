package com.kiosoft2.common.autoservice.impl

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.google.auto.service.AutoService
import com.kiosoft2.common.autoservice.service.TaskService
import com.kiosoft2.common.cache.CacheManager.getCacheTaskListByOwerClassName
import com.kiosoft2.common.cache.CacheManager.getCacheTaskListByOwerClassNameAndTaskId
import com.kiosoft2.common.cache.CacheManager.removeCacheTask
import com.kiosoft2.common.task.annotions.TaskBindDisposable
import com.kiosoft2.common.task.annotions.TaskComplete
import com.kiosoft2.common.task.interfaces.TaskReLoadCallback
import com.kiosoft2.common.task.model.TaskInfo
import com.kiosoft2.common.task.util.TaskManager
import com.kiosoft2.common.task.util.TaskMethodUtil.invokeMethod
import com.kiosoft2.common.task.util.TaskMethodUtil.invokeMethodTaskReLoad
import com.kiosoft2.common.task.util.TaskReLoadUtil.reLoadStart
import com.kiosoft2.common.task.util.TimeUtil.conventTimeByUnit
import com.kiosoft2.common.thead.annotions.RunInIO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AutoService(TaskService::class)
class TaskServiceImpl : TaskService {
    @RunInIO
    override fun reStartTask(`object`: Any) {
        GlobalScope.launch(Dispatchers.Main) {
            for (taskInfo in getCacheTaskListByOwerClassName(`object`.javaClass.name)) {
                start(`object`, taskInfo)
            }
        }

    }

    override fun reStartTask(`object`: Any, taskId: String) {
        GlobalScope.launch(Dispatchers.Main) {
            val taskInfo = getCacheTaskListByOwerClassNameAndTaskId(`object`.javaClass.name, taskId)
            taskInfo?.let { start(`object`, it) }
        }
    }

    private fun start(`object`: Any, taskInfo: TaskInfo) {
        //校验时间是否过期
        if (taskInfo.endTimeMillis < System.currentTimeMillis()) {
            GlobalScope.launch(Dispatchers.Main) {
                Log.d("lance", "${taskInfo.taskId}       任务已过期 移除 ")
                removeCacheTask(taskInfo)
            }
            return
        }
        //将是结束时间点转换为时间长度
        taskInfo.time =
            conventTimeByUnit(taskInfo.endTimeMillis - System.currentTimeMillis(), taskInfo.unit)
        if (`object` is LifecycleOwner) taskInfo.lifecycleOwner = `object`
        Log.d("lance", "${taskInfo.taskId}       重新执行 时长:    ${taskInfo.time} ")
        reLoadStart(`object`, taskInfo, object : TaskReLoadCallback {
            override fun onReLoadStart(taskInfo: TaskInfo) {
                invokeMethodTaskReLoad(
                    `object`,
                    taskInfo
                )
            }

            override fun bindDisposable(taskInfo: TaskInfo) {
                invokeMethod(
                    `object`,
                    TaskBindDisposable::class.java,
                    taskInfo
                )
            }

            override fun onTaskComplete(taskInfo: TaskInfo) {
                invokeMethod(
                    `object`,
                    TaskComplete::class.java,
                    taskInfo
                )
                TaskManager.removeTask(`object`, taskInfo)
            }
        })
    }

    override fun cancelTask(obj: Any) {
        Log.d(
            "lance",
            " ${obj.javaClass.name}   取消任务"
        )
        TaskManager.removeTask(obj)
    }

    override fun cancelTask(`object`: Any, taskId: String) {
        GlobalScope.launch(Dispatchers.Main) {
            val taskInfo = getCacheTaskListByOwerClassNameAndTaskId(`object`.javaClass.name, taskId)
            if (taskInfo != null) TaskManager.removeTask(`object`, taskInfo)
        }
    }
}