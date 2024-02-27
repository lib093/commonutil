package com.kiosoft2.testdemo.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kiosoft2.common.bus.LiveDataBus
import com.kiosoft2.common.click.ClickExe.setSafeOnClickListener
import com.kiosoft2.common.task.annotions.DelayedTask
import com.kiosoft2.common.task.annotions.RecurringTask
import com.kiosoft2.common.task.annotions.TaskThread
import com.kiosoft2.testdemo.databinding.ActivityTest02Binding
import java.util.concurrent.TimeUnit

class TestActivity02:AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var mBinding = ActivityTest02Binding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mBinding.btnNext.setSafeOnClickListener {
            startActivity(Intent(this@TestActivity02, TestActivity03::class.java))
            finish()
        }
        mBinding.btnPost.setSafeOnClickListener {
//            LiveDataBus.getInstance().with("test01",String::class.java).postValue("test02 ============》  test01")
//            LiveDataBus.getInstance().with("test03",String::class.java).postValue("test02 ============》  test03")
//            LiveDataBus.getInstance().with("TestILifecycleOwnerImpl",String::class.java).postValue("test02 ============》  TestILifecycleOwnerImpl")
        task()
            task2()
            delayedTask()
        }
        LiveDataBus.getInstance().with("test02",String::class.java).observe(this) {
            Toast.makeText(this@TestActivity02, it, Toast.LENGTH_SHORT).show()
            Log.d("lib934042435", "TestActivity02 收到消息 ${it}")
        }

    }



    override fun onStart() {
        super.onStart()
        Log.d("lib934042435", "onStart:  test02")
    }

    override fun onResume() {
        super.onResume()
        Log.d("lib934042435", "onResume:  test02")
        LiveDataBus.getInstance().with("test01",String::class.java).postValue("test02 ============》  test01")
        LiveDataBus.getInstance().with("test03",String::class.java).postValue("test02 ============》  test03")
        LiveDataBus.getInstance().with("TestILifecycleOwnerImpl",String::class.java).postValue("test02 ============》  TestILifecycleOwnerImpl")

    }

    override fun onStop() {
        super.onStop()
        Log.d("lib934042435", "onStop:  test02")
    }

    override fun onPause() {
        super.onPause()
        Log.d("lib934042435", "onPause:  test02")
    }
    @RecurringTask(taskCode = 1, initialDelay = 5,period = 6,time = 100, unit = TimeUnit.SECONDS, threadType = TaskThread.IO_THREAD)
    fun task(){
        Log.d("lib9340424351", "TestActivity02 task 任务当前线程 ：${Thread.currentThread().name}")
    }
    @RecurringTask(taskCode = 2,initialDelay = 5,period = 6,time = 100, threadType = TaskThread.MAIN_THREAD)
    fun task2(){
        Log.d("lib9340424351", "TestActivity02 task 2 任务当前线程 ：${Thread.currentThread().name}")
    }
    @DelayedTask(taskCode = 3,initialDelay = 20, unit = TimeUnit.SECONDS, threadType = TaskThread.IO_THREAD)
    fun delayedTask(){
        Log.d("lib9340424351", "TestActivity02 延时执行任务 ：${Thread.currentThread().name}")
    }

}