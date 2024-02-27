package com.kiosoft2.testdemo.bus.impl

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

abstract class ILifecycleOwner: LifecycleOwner {
    // 创建一个 LifecycleRegistry 实例
    val lifecycleRegistry = LifecycleRegistry(this)

    init {
        // 初始化 LifecycleRegistry，指定支持的生命周期状态
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
    }

    // 提供方法以改变生命周期状态
   open fun onStart() {
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
    }

    open fun onResume() {
        lifecycleRegistry.currentState = Lifecycle.State.RESUMED
    }
    open fun onPause() {
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
    }
    open fun onStop() {
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
    }
   open fun onDestroy() {
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }
}