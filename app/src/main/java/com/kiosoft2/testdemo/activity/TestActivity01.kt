package com.kiosoft2.testdemo.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kiosoft2.common.bus.LiveDataBus
import com.kiosoft2.common.click.ClickExe.setSafeOnClickListener
import com.kiosoft2.testdemo.databinding.ActivityTest01Binding
import com.kiosoft2.common.task.annotions.TaskBindDisposable
import com.kiosoft2.common.task.annotions.TaskComplete
import com.kiosoft2.common.task.annotions.TaskThread
import com.kiosoft2.common.task.interfaces.TaskInterface
import com.kiosoft2.common.thead.annotions.RunInIO
import com.kiosoft2.common.thead.annotions.RunInMainThread
import com.kiosoft2.common.task.annotions.RecurringTask
import com.kiosoft2.common.task.model.TaskInfo
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit


class TestActivity01 : AppCompatActivity(), TaskInterface {
    lateinit var task1:TaskInfo
    lateinit var task2:TaskInfo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var mBinding = ActivityTest01Binding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mBinding.btnNext.setOnClickListener {
            startActivity(Intent(this@TestActivity01, TestActivity02::class.java))
        }
        mBinding.btnPost.setSafeOnClickListener {
//            Log.d("lib934042435", "setOnClickListener")
//            LiveDataBus.getInstance().with("test02",String::class.java).postValue("test01 ============》  test02")
//            LiveDataBus.getInstance().with("test03",String::class.java).postValue("test01 ============》  test03")
//            LiveDataBus.getInstance().with("TestILifecycleOwnerImpl",String::class.java).postValue("test01 ============》  TestILifecycleOwnerImpl")
//            onPost(10)
            task()
            task2()
        }
        LiveDataBus.getInstance().with("test01",String::class.java).observe(this) {
            Toast.makeText(this@TestActivity01, it, Toast.LENGTH_SHORT).show()
            Log.d("lib934042435", "TestActivity01 收到消息 ${it}")
        }
    }
//    @SingleTime(10000)
    @RunInIO
     fun onPost(num:Int){
         Log.d("lib934042435", "onPost ${num}")
         Log.d("lib9340424351", "thread ${Thread.currentThread().name}")
        onSend(num)
     }
    @RunInMainThread
    fun onSend(num:Int){
        Log.d("lib9340424351", "onSend ${num}   ${Thread.currentThread().name}")
    }
    override fun onStart() {
        super.onStart()
        Log.d("lib934042435", "onStart:  test01")
    }

    override fun onResume() {
        super.onResume()
//        Log.d("lib934042435", "onResume:  test01")
//        LiveDataBus.getInstance().with("test02",String::class.java).postValue("test01 ============》  test02")
//        LiveDataBus.getInstance().with("test03",String::class.java).postValue("test01 ============》  test03")
//        LiveDataBus.getInstance().with("TestILifecycleOwnerImpl",String::class.java).postValue("test01 ============》  TestILifecycleOwnerImpl")

    }

    override fun onStop() {
        super.onStop()
        Log.d("lib934042435", "onStop:  test01")
    }

    override fun onPause() {
        super.onPause()
        Log.d("lib934042435", "onPause:  test01")
        LiveDataBus.getInstance().with("test01",String::class.java).removeObservers(this@TestActivity01)
    }
    interface onPost{
        fun onPost2(num:Int)
    }

    @RecurringTask(taskCode = 1,initialDelay = 1,period = 1,time = 20, isRepeat = false ,threadType = TaskThread.IO_THREAD)
    fun task(){
        Log.d("lib9340424351", "TestActivity01 task 1 任务当前线程 ：${Thread.currentThread().name}")
    }
    @RecurringTask(taskCode = 2,initialDelay = 1,period = 1,time = 20, threadType = TaskThread.MAIN_THREAD)
    fun task2(){
        Log.d("lib9340424351", "TestActivity01  task 2 任务当前线程 ：${Thread.currentThread().name}")
    }
    @TaskBindDisposable
    override fun bindDisposable(taskInfo: TaskInfo) {
        if (taskInfo.taskCode == 1){
            task1 = taskInfo
        }else if (taskInfo.taskCode == 2){
            task2 = taskInfo
        }
    }
    @TaskComplete
    override fun onTaskComplete(taskInfo: TaskInfo) {
    }

}