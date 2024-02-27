package com.kiosoft2.testdemo.bus.impl

import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

interface ILifecycleEventObserver : LifecycleEventObserver {
    fun bindLifecycleOwner(lifecycleOwner: LifecycleOwner)
}