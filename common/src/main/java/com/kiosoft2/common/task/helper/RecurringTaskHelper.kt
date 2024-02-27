package com.kiosoft2.common.task.helper

import android.util.Log
import com.kiosoft2.common.task.annotions.DelayedTask
import com.kiosoft2.common.task.annotions.RecurringTask
import com.kiosoft2.common.task.annotions.TaskBindDisposable
import com.kiosoft2.common.task.annotions.TaskComplete
import com.kiosoft2.common.task.aspect.threadType
import com.kiosoft2.common.task.interfaces.JoinPointRunCallback
import com.kiosoft2.common.task.model.TaskInfo
import com.kiosoft2.common.task.util.TaskManager
import com.kiosoft2.common.task.util.TaskMethodUtil
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

class RecurringTaskHelper {
    fun start(obj: Any,
              recurringTask: RecurringTask,
              taskInfo: TaskInfo,
              callBack: JoinPointRunCallback
    ){
        var disposable: Disposable? = null
        Observable.interval(
            recurringTask.initialDelay,
            recurringTask.period,
            recurringTask.unit
        )
            .take(recurringTask.time)
            .threadType(recurringTask.threadType)
            .subscribe(object : Observer<Any> {
                override fun onSubscribe(d: Disposable) {
                    disposable = d
                    taskInfo.setDisposable(disposable!!)//disposable 任务订阅者
                    TaskMethodUtil.invokeMethod(
                        obj,
                        TaskBindDisposable::class.java,
                        taskInfo
                    )
                    TaskManager.putTask(
                        obj,
                        taskInfo
                    )//绑定声明周期
                }

                override fun onError(e: Throwable) {
                    Log.e("Task", "onError: ${e.message}")
                    TaskManager.removeTask(obj, taskInfo)
                }

                override fun onComplete() {
                    TaskMethodUtil.invokeMethod(
                        obj,
                        TaskComplete::class.java,
                        taskInfo
                    )
                    TaskManager.removeTask(obj, taskInfo)
                }

                override fun onNext(t: Any) {
                    callBack.run()
                }

            })
    }
}