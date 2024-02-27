package com.kiosoft2.common.thead.annotions

import androidx.annotation.Keep

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@Keep
annotation class RunInIO(
)
