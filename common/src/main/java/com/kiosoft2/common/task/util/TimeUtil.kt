package com.kiosoft2.common.task.util

import java.util.concurrent.TimeUnit

object TimeUtil {
    /**
     * 时间转化
     */
    fun conventTimeMillis(time: Long, unit: TimeUnit): Long {
        return when (unit) {
            TimeUnit.SECONDS -> time * 1000
            TimeUnit.MINUTES -> time * 1000 * 60
            TimeUnit.HOURS -> time * 1000 * 60 * 60
            TimeUnit.DAYS -> time * 1000 * 60 * 60 * 24
            else -> time
        }
    }

    /***
     * 将毫秒转化为指定单位的时间
     */
    fun conventTimeByUnit(time: Long, unit: TimeUnit): Long {
        return when (unit) {
            TimeUnit.SECONDS -> time / 1000
            TimeUnit.MINUTES -> time / 1000 / 60
            TimeUnit.HOURS -> time / 1000 / 60 / 60
            TimeUnit.DAYS -> time / 1000 / 60 / 60 / 24
            else -> time
        }

    }
}