package com.kiosoft2.common.task.interfaces

import androidx.annotation.Keep
import com.kiosoft2.common.task.model.TaskInfo

/**
 * 重新加载任务回调
 */
@Keep
interface TaskReLoadCallback:TaskInterface {
    //重新加载运行
    fun onReLoadStart(taskInfo: TaskInfo)
}