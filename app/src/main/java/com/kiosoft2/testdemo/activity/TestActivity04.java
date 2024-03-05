package com.kiosoft2.testdemo.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.kiosoft2.annotation.AParameter;
import com.kiosoft2.api.aparameter.AParameterManager;
import com.kiosoft2.common.router.ARouterOperator;
import com.kiosoft2.testdemo.R;
import com.kiosoft2.common.click.ClickExe;
import com.kiosoft2.common.task.annotions.DelayedTask;
import com.kiosoft2.common.task.annotions.TaskBindDisposable;
import com.kiosoft2.common.task.annotions.TaskComplete;
import com.kiosoft2.common.task.annotions.TaskThread;
import com.kiosoft2.common.task.annotions.RecurringTask;
import com.kiosoft2.testdemo.Test1Fragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.disposables.Disposable;


public class TestActivity04 extends AppCompatActivity {
    Disposable task1;
    Disposable task2s;
    @AParameter
    public String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test01);
        AParameterManager.Companion.getInstance().load(this);
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
        Fragment f = ARouterOperator.Companion.op(Test1Fragment.class).withString("name","李四").navigation();
//        Toast.makeText(this, f, Toast.LENGTH_SHORT).show();
//        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("lib934042435", "onClick: ");
////               Toast.makeText(TestActivity04.this, "点击率", Toast.LENGTH_SHORT).show();
//            }
//        });
        ClickExe.setSafeOnClickListener(findViewById(R.id.btn_next),new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("lib9340424351", "onClick: ");
                task();
                task2();
                delayedTask();
            }});
        }
    @RecurringTask(taskCode = 1, initialDelay = 2,period = 3,time = 100, unit = TimeUnit.SECONDS, threadType = TaskThread.IO_THREAD)
    void task(){
        Log.d("lib9340424351", "task任务当前线程 ："+Thread.currentThread().getName());
    }
    @RecurringTask(taskCode = 2,initialDelay = 2,period = 3,time = 100, unit = TimeUnit.SECONDS, threadType = TaskThread.MAIN_THREAD)
    void task2(){
        Log.d("lib9340424351", "2222 task任务当前线程 ："+Thread.currentThread().getName());
    }
    @DelayedTask(taskCode = 3,initialDelay = 30, unit = TimeUnit.SECONDS, threadType = TaskThread.IO_THREAD)
    void delayedTask(){
        Log.d("lib9340424351", "延时执行任务 ："+Thread.currentThread().getName());
    }

    @TaskBindDisposable
    void  bindDisposable(Disposable disposable, int taskCode) {
        Log.d("lib9340424351", "taskCode ："+taskCode);
        if (taskCode == 1){
            task1 = disposable;
        }else if (taskCode == 2){
            task2s = disposable;
        }
    }
    @TaskComplete
    void  onTaskComplete(int taskCode) {
        Log.d("lib9340424351", "onTaskComplete "+taskCode);
    }

}