package com.kiosoft2.common.task.annotions

import androidx.annotation.Keep
import java.util.concurrent.TimeUnit

/**
 * 延时执行任务
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@Keep
annotation class DelayedTask(
    val taskCode: Int = 0, //任务编号
    val initialDelay: Long = 0, //初始延迟时间
    val threadType: TaskThread,
    val unit: TimeUnit = TimeUnit.SECONDS, //时间单位
    val isRepeat: Boolean = false, //是否重复执行(false:不重复执行,有任务正在执行的话，不在开启新的任务，继续执行已存在的任务 true:重复执行，有任务正在执行的话，将再开启新的任务)
    val isReStart: Boolean = false, //是否重新执行(isRepeat = false 时生效  true:重新执行(未执行完的任务 重新执行)
    val isCacheResumeStart: Boolean = false //是继续执行(未执行完的任务进行持久化后 继续执行)
)
