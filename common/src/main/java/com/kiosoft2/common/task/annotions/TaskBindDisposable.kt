package com.kiosoft2.common.task.annotions

import androidx.annotation.Keep

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@Keep
annotation class TaskBindDisposable()
