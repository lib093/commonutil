package com.kiosoft2.common.model

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.kiosoft2.common.bus.LiveDataBus
import com.kiosoft2.testdemo.bus.impl.ILifecycleEventObserver
import com.kiosoft2.testdemo.bus.impl.ILifecycleOwner

open class BaseLifecycleOwnerImpl : ILifecycleOwner(), ILifecycleEventObserver {
    override val lifecycle: Lifecycle
        get() = super.lifecycleRegistry

    init {
        LiveDataBus.getInstance().with("TestILifecycleOwnerImpl", String::class.java)
            .observe(this) {
               // Toast.makeText(MyApplication.getInstance(), it, Toast.LENGTH_SHORT).show()
                Log.d("lance", "TestILifecycleOwnerImpl 收到消息 ${it}")
            }
    }

    override fun bindLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                Log.d("lance", "TestILifecycleOwnerImpl 收到消息 ON_CREATE")
            }

            Lifecycle.Event.ON_START -> {
                Log.d("lance", "TestILifecycleOwnerImpl 收到消息 ON_START")
                onStart()
            }

            Lifecycle.Event.ON_RESUME -> {
                Log.d("lance", "TestILifecycleOwnerImpl 收到消息 ON_RESUME")
                this.onResume()
            }

            Lifecycle.Event.ON_PAUSE -> {
                Log.d("lance", "TestILifecycleOwnerImpl 收到消息 ON_PAUSE")
                this.onPause()
            }

            Lifecycle.Event.ON_STOP -> {
                Log.d("lance", "TestILifecycleOwnerImpl 收到消息 ON_STOP")
                this.onStop()
            }

            Lifecycle.Event.ON_DESTROY -> {
                Log.d("lance", "TestILifecycleOwnerImpl 收到消息 ON_DESTROY")
                this.onDestroy()
            }

            Lifecycle.Event.ON_ANY -> {
                Log.d("lance", "TestILifecycleOwnerImpl 收到消息 ON_ANY")
            }
        }
    }

}