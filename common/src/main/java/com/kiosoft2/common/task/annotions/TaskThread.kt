package com.kiosoft2.common.task.annotions

import java.io.Serializable


enum class TaskThread:Serializable {
    MAIN_THREAD,
    CURR_THREAD,
    IO_THREAD;
}