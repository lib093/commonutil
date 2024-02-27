package com.kiosoft2.testdemo.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kiosoft2.common.bus.LiveDataBus
import com.kiosoft2.common.click.ClickExe.setSafeOnClickListener
import com.kiosoft2.common.click.annotions.ClickFilter
import com.kiosoft2.testdemo.databinding.ActivityTest03Binding
import com.kiosoft2.common.model.BaseLifecycleOwnerImpl
import com.kiosoft2.common.task.annotions.DelayedTask
import com.kiosoft2.common.task.annotions.RecurringTask
import com.kiosoft2.common.task.annotions.TaskThread
import java.util.concurrent.TimeUnit

class TestActivity03:AppCompatActivity(){
    var baseLifecycleOwnerImpl = BaseLifecycleOwnerImpl()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var mBinding = ActivityTest03Binding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mBinding.btnNext.setOnClickListener {
            startActivity(Intent(this@TestActivity03, TestActivity02::class.java))
        }
        mBinding.btnPost.setSafeOnClickListener(object :OnClickListener{
            @ClickFilter
            override fun onClick(v: View?) {
//                LiveDataBus.getInstance().with("test02",String::class.java).postValue("test03 ============》  test02")
//                LiveDataBus.getInstance().with("test01",String::class.java).postValue("test03 ============》  test01")
//                LiveDataBus.getInstance().with("TestILifecycleOwnerImpl",String::class.java).postValue("test03 ============》  TestILifecycleOwnerImpl")
                    task()
                task2()
                delayedTask()
            }

        })
        LiveDataBus.getInstance().with("test03",String::class.java).observe(this) {
            Toast.makeText(this@TestActivity03, it, Toast.LENGTH_SHORT).show()
            Log.d("lib934042435", "TestActivity03 收到消息 ${it}")
        }
        baseLifecycleOwnerImpl.bindLifecycleOwner(this@TestActivity03)
    }
    override fun onStart() {
        super.onStart()
        Log.d("lib934042435", "onStart:  test03")
    }

    override fun onResume() {
        super.onResume()
        Log.d("lib934042435", "onResume:  test03")
        LiveDataBus.getInstance().with("test02",String::class.java).postValue("test03 ============》  test02")
        LiveDataBus.getInstance().with("test01",String::class.java).postValue("test03 ============》  test01")
        LiveDataBus.getInstance().with("TestILifecycleOwnerImpl",String::class.java).postValue("test03 ============》  TestILifecycleOwnerImpl")
    }

    override fun onStop() {
        super.onStop()
        Log.d("lib934042435", "onStop:  test03")
    }

    override fun onPause() {
        super.onPause()
        Log.d("lib934042435", "onPause:  test03")
        LiveDataBus.getInstance().with("test03",String::class.java).removeObservers(this@TestActivity03)
    }
    @RecurringTask(taskCode = 1, initialDelay = 5,period = 6,time = 100, unit = TimeUnit.SECONDS, threadType = TaskThread.IO_THREAD)
    fun task(){
        Log.d("lib9340424351", "TestActivity03 task 任务当前线程 ：${Thread.currentThread().name}")
    }
    @RecurringTask(taskCode = 2,initialDelay = 5,period = 6,time = 100, unit = TimeUnit.SECONDS, threadType = TaskThread.MAIN_THREAD)
    fun task2(){
        Log.d("lib9340424351", "TestActivity03 task 2 任务当前线程 ：${Thread.currentThread().name}")
    }
    @DelayedTask(taskCode = 3,initialDelay = 20, unit = TimeUnit.SECONDS, threadType = TaskThread.IO_THREAD)
    fun delayedTask(){
        Log.d("lib9340424351", "TestActivity03 延时执行任务 ：${Thread.currentThread().name}")
    }
}