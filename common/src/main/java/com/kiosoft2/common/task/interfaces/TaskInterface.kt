package com.kiosoft2.common.task.interfaces

import androidx.annotation.Keep
import com.kiosoft2.common.task.annotions.TaskBindDisposable
import com.kiosoft2.common.task.annotions.TaskComplete
import com.kiosoft2.common.task.model.TaskInfo
import io.reactivex.rxjava3.disposables.Disposable

interface TaskInterface {
    @TaskBindDisposable
    fun bindDisposable(taskInfo: TaskInfo)
    @TaskComplete
    fun onTaskComplete(taskInfo: TaskInfo)
}