package com.kiosoft2.common.click.annotions

import androidx.annotation.Keep

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@Keep
annotation class SingleTime(
    val time: Long = 800
)
