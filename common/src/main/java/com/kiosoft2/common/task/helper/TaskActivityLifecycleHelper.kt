package com.kiosoft2.common.task.helper

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.kiosoft2.common.cache.TaskCacheManager
import com.kiosoft2.common.task.annotions.TaskBindDisposable
import com.kiosoft2.common.task.annotions.TaskComplete
import com.kiosoft2.common.task.interfaces.TaskReLoadCallback
import com.kiosoft2.common.task.model.TaskInfo
import com.kiosoft2.common.task.util.TaskReLoadUtil
import com.kiosoft2.common.task.util.TaskManager
import com.kiosoft2.common.task.util.TaskMethodUtil
import com.kiosoft2.common.task.util.TimeUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TaskActivityLifecycleHelper(taskManager:TaskManager): Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        GlobalScope.launch(Dispatchers.Main) {
                //获取任务管理器
            TaskCacheManager.getCacheTaskListByOwerClassName(activity.javaClass.name)?.forEach {
                    //校验时间是否过期
                    if (it.endTimeMillis < System.currentTimeMillis()) {
                        TaskCacheManager.removeCacheTask(it)
                        return@forEach
                    }
                    //将是结束时间点转换为时间长度
                    it.time = TimeUtil.conventTimeByUnit(
                        it.endTimeMillis - System.currentTimeMillis(),
                        it.unit
                    )
                    it.lifecycleOwner = activity as LifecycleOwner
                    TaskReLoadUtil.reLoadStart(activity, it, object :
                        TaskReLoadCallback {
                        override fun onReLoadStart(taskInfo: TaskInfo) {
                            TaskMethodUtil.invokeMethodTaskReLoad(
                                activity,
                                taskInfo
                            )
                        }

                        override fun bindDisposable(taskInfo: TaskInfo) {
                            TaskMethodUtil.invokeMethod(
                                activity,
                                TaskBindDisposable::class.java,
                                taskInfo
                            )
                        }

                        override fun onTaskComplete(taskInfo: TaskInfo) {
                            TaskMethodUtil.invokeMethod(
                                activity,
                                TaskComplete::class.java,
                                taskInfo
                            )
                            TaskManager.removeTask(activity, taskInfo)
                        }
                    })
                }
        }

    }

    override fun onActivityStarted(activity: Activity) {
        Log.d("lance", "onActivityStarted:    ${activity.javaClass.name} : ")
    }

    override fun onActivityResumed(activity: Activity) {
        Log.d("lance", "onActivityResumed:    ${activity.javaClass.name} : ")
    }

    override fun onActivityPaused(activity: Activity) {
        Log.d("lance", "onActivityPaused:    ${activity.javaClass.name} : ")
    }

    override fun onActivityStopped(activity: Activity) {
        Log.d("lance", "onActivityStopped:    ${activity.javaClass.name} : ")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Log.d("lance", "onActivitySaveInstanceState:    ${activity.javaClass.name} : ")
    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.d("lance", "onActivityDestroyed:    ${activity.javaClass.name} : ")
    }
}