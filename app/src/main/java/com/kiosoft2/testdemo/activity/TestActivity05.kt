package com.kiosoft2.testdemo.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kiosoft2.annotation.AParameter
import com.kiosoft2.api.RoomOperator
import com.kiosoft2.api.type.OrderByType
import com.kiosoft2.common.autoservice.ServiceLoad
import com.kiosoft2.common.autoservice.service.TaskService
import com.kiosoft2.common.click.ClickExe.setSafeOnClickListener
import com.kiosoft2.common.click.annotions.SingleTime
import com.kiosoft2.common.encrypt.EncryptService
import com.kiosoft2.common.router.ARouterOperator
import com.kiosoft2.testdemo.databinding.ActivityTest01Binding
import com.kiosoft2.common.task.annotions.DelayedTask
import com.kiosoft2.common.task.annotions.TaskBindDisposable
import com.kiosoft2.common.task.annotions.TaskComplete
import com.kiosoft2.common.task.annotions.TaskThread
import com.kiosoft2.common.task.annotions.RecurringTask
import com.kiosoft2.common.task.interfaces.TaskReLoadCallback
import com.kiosoft2.common.task.model.TaskInfo
import com.kiosoft2.common.thead.annotions.RunInIO
import com.kiosoft2.testdemo.db.model.Book
import com.kiosoft2.testdemo.db.model.Book_
import com.kiosoft2.testdemo.db.model.User
import com.kiosoft2.testdemo.db.model.User_
import com.ubix.kiosoft2.db.DBOperator
import java.util.concurrent.TimeUnit

class TestActivity05 : AppCompatActivity(), TaskReLoadCallback {
    lateinit var task1:TaskInfo
    lateinit var task2s:TaskInfo
    @AParameter
    var name:String? = null

    /**
     * 任务缓存服务
     */
    var taskService: TaskService? = lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        ServiceLoad.load(TaskService::class.java)
    }.value
    var encryptService: EncryptService? = lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        ServiceLoad.load(EncryptService::class.java)
    }.value
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var mBinding = ActivityTest01Binding.inflate(layoutInflater)
        setContentView(mBinding.root)
//        /**
//         * 任务缓存服务
//         */
//        var taskService: TaskService? = lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
//            ServiceLoad.load(TaskService::class.java)
//        }.value
        mBinding.btnNext.setSafeOnClickListener {
           // startActivity(Intent(this@TestActivity05,TestActivity03::class.java))
//            Log.d("lance", "加密测试: ${encryptService?.getEncrypt("121212")}")
            ARouterOperator.op(TestActivity04::class.java).withString("name","张三").withBoolean("ss",false).navigation(this,false)
        }
        mBinding.btnStop.setOnClickListener {
//            testSingleTime()
//            Log.d(
//                "lance",
//                " taskService == null  ${ taskService == null }"
//            )
            taskService?.cancelTask(this@TestActivity05)
            testSelect()
        }
        mBinding.btnPost.setOnClickListener {
            testDBInset()
//                delayedTask()
//                delayedTask2()
//                testLifcycle.task()
//            Log.d("lance", "setOnClickListener")
//            delayedTask()
//            if (!::task1.isInitialized || task1.isRunning()){
//                Log.d("lance", "task1执行")
//                task();
//            }else{
//                Log.d("lance", "task1正在执行")
//            }
//
//            if (!::task2s.isInitialized || task2s.isRunning()){
//                Log.d("lance", "task1执行")
//                task2();
//            }else{
//                Log.d("lance", "task1正在执行")
//            }
            task3();
            task4();
//            task5();
//            task6();
//            task7();
//            task8();
        }

    }
    @RunInIO
    fun testDBInset(){

        val randUser = arrayOf(
            "刘德华",
            "张三",
            "Richard",
            "Jerry",
            "李四",
            "成龙",
            "周星驰",
            "关晓彤",
            "肖战",
            "赵璐思"
        )
        val userList: MutableList<User> = ArrayList<User>()
        for (j in 0..99999) {
            val user = User()
            user.setName(randUser[(Math.random() * 10).toInt()])
            user.setAge((Math.random() * 100).toInt())
            userList.add(user)
        }

        val startTime = System.currentTimeMillis()
        val uidList: List<Long> = RoomOperator.op(User::class.java).save(userList)
        Log.d("lance",  String.format(
            "=======保存User[%s条]耗时%s ms=======>",
            uidList.size,
            System.currentTimeMillis() - startTime
        ))
        Log.d("lance", "testDBInset: ")
        val bookList: MutableList<Book> = ArrayList<Book>()
        for (j in 0..99999) {
            val book = Book()
            book.setName(randUser[(Math.random() * 10).toInt()])
            book.setPage((Math.random() * 100).toInt())
            book.uid = j.toLong()
            bookList.add(book)
        }
        val startTime2 = System.currentTimeMillis()
        DBOperator.get()?.getBookDao()?.insertBooks(bookList)
        Log.d("lance",  String.format(
            "=======保存book[%s条]耗时%s ms=======>",
            bookList.size,
            System.currentTimeMillis() - startTime2
        ))
    }
    @RunInIO
    fun testSelect(){
        Log.d("lance", "testSelect: ")
        val startTime = System.currentTimeMillis()
//        val selectAll = DBOperator.get()?.getBookDao()?.selectAll()
        val data = RoomOperator
            .op(User::class.java)
            .innerJoin(Book_._instance)
            .equal(User_.id, Book_.uid)
            .query()
            .contains(User_.name, "周星驰")
            .build()
            .page(100, 1)
            .orderBy(User_.age, OrderByType.desc)
            .find()
        Log.d("lance",  String.format(
            "=======data[%s条]耗时%s ms=======>",
            data?.size,
            System.currentTimeMillis() - startTime
        ))
    }
    @SingleTime
    fun testSingleTime(){
        Log.d("lance", "testSingleTime ：${Thread.currentThread().name}")
    }
    @RecurringTask(taskCode = 1, initialDelay = 1,period = 1,time = 10, isRepeat = true, unit = TimeUnit.SECONDS, threadType = TaskThread.IO_THREAD)
    fun task(){
        Log.d("lance", "task任务当前线程 ：${Thread.currentThread().name}")
    }
    @RecurringTask(taskCode = 3, initialDelay = 1,period = 1,time = 200,isRepeat = false, isReStart = true, unit = TimeUnit.SECONDS, threadType = TaskThread.IO_THREAD, isCacheResumeStart = true)
    fun task3(str: String = "123",age:Int = 12){
        Log.d("lance", "task 3 任务当前线程 ：${Thread.currentThread().name}")
    }
    @RecurringTask(taskCode = 4, initialDelay = 1,period = 1,time = 10,isRepeat = false, unit = TimeUnit.SECONDS, threadType = TaskThread.IO_THREAD)
    fun task4(){
        Log.d("lance", "task 4 任务当前线程 ：${Thread.currentThread().name}")
    }
    @RecurringTask(taskCode = 5, initialDelay = 1,period = 1,time = 10,isRepeat = true, unit = TimeUnit.SECONDS, threadType = TaskThread.IO_THREAD)
    fun task5(){
        Log.d("lance", "task 5 任务当前线程 ：${Thread.currentThread().name}")
    }
    @RecurringTask(taskCode = 6, initialDelay = 1,period = 1,time = 10,isRepeat = true, unit = TimeUnit.SECONDS, threadType = TaskThread.MAIN_THREAD)
    fun task6(){
        Log.d("lance", "task 6 任务当前线程 ：${Thread.currentThread().name}")
    }
    @RecurringTask(taskCode = 7, initialDelay = 1,period = 1,time = 10,isRepeat = false, unit = TimeUnit.SECONDS, threadType = TaskThread.IO_THREAD)
    fun task7(){
        Log.d("lance", "task 7 任务当前线程 ：${Thread.currentThread().name}")
    }
    @RecurringTask(taskCode = 8, initialDelay = 1,period = 1,time = 10,isRepeat = true, unit = TimeUnit.SECONDS, threadType = TaskThread.IO_THREAD)
    fun task8(){
        Log.d("lance", "task 8 任务当前线程 ：${Thread.currentThread().name}")
    }
    @RecurringTask(taskCode = 2,initialDelay = 1,period = 1,time = 200, unit = TimeUnit.SECONDS, threadType = TaskThread.MAIN_THREAD)
    fun task2(){
        Log.d("lance", "2222 task任务当前线程 ：${Thread.currentThread().name}")
    }
    @DelayedTask(taskCode = 3,initialDelay = 20, unit = TimeUnit.SECONDS, threadType = TaskThread.IO_THREAD)
    fun delayedTask(){
        Log.d("lance", "延时执行任务 ：${Thread.currentThread().name}")
    }
    @DelayedTask(taskCode = 4,initialDelay = 10, unit = TimeUnit.SECONDS, threadType = TaskThread.MAIN_THREAD)
    fun delayedTask2(){
        Log.d("lance", "延时执行任务 ：${Thread.currentThread().name}")
    }

    override fun onReLoadStart(taskInfo: TaskInfo) {
        Log.d("lance", "onReLoadStart 任务当前线程 ：${Thread.currentThread().name}  ${taskInfo.taskId}")
    }


    @TaskBindDisposable
    override fun bindDisposable(taskInfo: TaskInfo) {
        Log.d("lance", "taskCode ：${taskInfo.taskCode}")
        if (taskInfo.taskCode == 1){
            task1 = taskInfo
        }else if (taskInfo.taskCode == 2){
            task2s = taskInfo
        }
    }
    @TaskComplete
    override fun onTaskComplete(taskInfo: TaskInfo) {
        Log.d("lance", "onTaskComplete ${taskInfo.taskCode}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("lance", " TestActivity05 onDestroy 页面关闭")
    }

}