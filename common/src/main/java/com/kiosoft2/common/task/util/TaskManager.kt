package com.kiosoft2.common.task.util

import android.app.Application
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.kiosoft2.common.cache.TaskCacheManager
import com.kiosoft2.common.task.helper.TaskActivityLifecycleHelper
import com.kiosoft2.common.task.model.TaskInfo
import com.kiosoft2.testdemo.bus.impl.ILifecycleEventObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

object TaskManager : ILifecycleEventObserver {
    // cacheHashMap >= lifecycleMap

    @Volatile
    var lifecycleMap = ConcurrentHashMap<LifecycleOwner, CopyOnWriteArrayList<TaskInfo>>()
    @Volatile
    private var cacheHashMap = ConcurrentHashMap<String, CopyOnWriteArrayList<TaskInfo>>()
    var taskActivityLifecycleHelper: TaskActivityLifecycleHelper = lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        TaskActivityLifecycleHelper(this)
    }.value
    fun init(application: Application){
        TaskCacheManager.init(application)
        application.registerActivityLifecycleCallbacks(taskActivityLifecycleHelper)
    }
    @Synchronized
    fun isExistTask(obj: Any,taskCode:Int, annotationClass: Class<out Annotation>): Boolean {
        var list = cacheHashMap[obj.javaClass.name]
        if (null == list) {
            return false
        }
        var removeLifecycleMap = obj is LifecycleOwner
        var isExist = false
        list?.forEach {
                if ( it.taskCode == taskCode && it.annotationClass == annotationClass) { //形同编号 相同注解
                    if (it.isRunning()) {
                        isExist = true
                    }else {
                        GlobalScope.launch { TaskCacheManager.removeCacheTask(it) }
                        if (removeLifecycleMap) {
                            lifecycleMap[obj as LifecycleOwner]?.remove(it)
                        }
                        list.remove(it)
                    }
                }
        }

        return isExist
    }
    @Synchronized
    fun putTask(obj: Any, taskInfo: TaskInfo) {
        // cacheHashMap >= lifecycleMap
        cacheTask(obj, taskInfo)
        if (null == taskInfo.lifecycleOwner) {
            return
        }
        bindLifecycle(obj as LifecycleOwner, taskInfo)
    }

    /**
     * 移除任务
     */
    @Synchronized
    fun removeTask(o: Any, taskInfo: TaskInfo) {
        val cacheList = cacheHashMap[o.javaClass.name]
        var removeLifecycleMap = o is LifecycleOwner

        // 多线程并发安全遍历和删除
        cacheList?.let { list->
            for (it in list) {
                if ((it.taskCode == taskInfo.taskCode && it.annotationClass == taskInfo.annotationClass && taskInfo.isReStart)|| (it.taskId == taskInfo.taskId )) {
                    if (it.isRunning()) {
                        it.cancleTask()
                    }
                    if (removeLifecycleMap) {
                        lifecycleMap[o as LifecycleOwner]?.remove(it)
                    }
                    GlobalScope.launch(Dispatchers.Main) {
                        TaskCacheManager.removeCacheTask(it)
                    }
                    cacheList.remove(it)
                }
            }
        }

}   @Synchronized
    fun removeTask(o: Any){
        val cacheList = cacheHashMap[o.javaClass.name]
        var removeLifecycleMap = o is LifecycleOwner
if (cacheList == null || cacheList.size == 0) {
            return
        }
        // 多线程并发安全遍历和删除
        cacheList?.let { list->
            for (it in list) {
                if (it.isRunning()) {
                    it.cancleTask()
                }
                if (removeLifecycleMap) {
                    lifecycleMap[o as LifecycleOwner]?.remove(it)
                }
                if (it.isCacheResumeStart)
                    GlobalScope.launch(Dispatchers.Main) {
                        TaskCacheManager.removeCacheTask(it)
                    }
                cacheList.remove(it)
            }
        }

    }
private fun cacheTask(obj: Any, taskInfo: TaskInfo) {
    var list = cacheHashMap[obj.javaClass.name]
    if (null == list) {
        list = CopyOnWriteArrayList()
    }
    if (obj is LifecycleOwner) {
        taskInfo.lifecycleOwner = obj as LifecycleOwner
    }
    list.add(taskInfo)
    cacheHashMap[obj.javaClass.name] = list
    if (taskInfo.isCacheResumeStart) {
        //持久化
        TaskCacheManager.cacheTask(taskInfo)
    }
}

/**
 * 将任务绑定生命周期
 */
private fun bindLifecycle(lifecycleOwner: LifecycleOwner, taskInfo: TaskInfo) {
    var list = lifecycleMap[lifecycleOwner]
    if (null == list) {
        list = CopyOnWriteArrayList()
    }
    list.add(taskInfo)
    lifecycleMap[lifecycleOwner] = list
    bindLifecycleOwner(lifecycleOwner)
}

/**
 * 根据LifecycleOwner 取消任务
 */
private fun cancleLifecycleTaskByLifecycleOwner(lifecycleOwner: LifecycleOwner) {
    lifecycleMap[lifecycleOwner]?.let {
        it.forEach {
            if (it.isRunning()) {
                it.cancleTask()
            }
        }
        lifecycleMap.remove(lifecycleOwner)
    }
}

override fun bindLifecycleOwner(lifecycleOwner: LifecycleOwner) {
    lifecycleOwner.lifecycle.addObserver(this)
}

override fun onStateChanged(lifecycleOwner: LifecycleOwner, event: Lifecycle.Event) {
    when (event) {
        Lifecycle.Event.ON_CREATE -> {
            Log.d("lance", "TaskLifecycleManager 收到消息  ${lifecycleOwner.javaClass.name}   ON_CREATE")
        }

        Lifecycle.Event.ON_START -> {
            Log.d("lance", "TaskLifecycleManager 收到消息   ${lifecycleOwner.javaClass.name}   ON_START")

        }

        Lifecycle.Event.ON_RESUME -> {
            Log.d("lance", "TaskLifecycleManager 收到消息   ${lifecycleOwner.javaClass.name}   ON_RESUME")

        }

        Lifecycle.Event.ON_PAUSE -> {
            Log.d("lance", "TaskLifecycleManager 收到消息   ${lifecycleOwner.javaClass.name}   ON_PAUSE")

        }

        Lifecycle.Event.ON_STOP -> {
            Log.d("lance", "TaskLifecycleManager 收到消息   ${lifecycleOwner.javaClass.name}   ON_STOP")

        }

        Lifecycle.Event.ON_DESTROY -> {
            Log.d("lance", "TaskLifecycleManager 收到消息   ${lifecycleOwner.javaClass.name}   ON_DESTROY")
            cancleLifecycleTaskByLifecycleOwner(lifecycleOwner) //取消lifecycleOwner所关联的任务
        }

        Lifecycle.Event.ON_ANY -> {
            Log.d("lance", "TaskLifecycleManager 收到消息 ON_ANY")
        }

        else -> {}
    }
}
}