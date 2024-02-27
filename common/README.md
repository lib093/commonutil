# 任务

## 	**@RecurringTask**  **周期性任务**

```kotlin
annotation class RecurringTask(
                      val taskCode: Int, //自定义任务编号
                      val threadType: TaskThread,
                      val initialDelay: Long = 0, //初始延迟时间
                      val period: Long = 0, //任务执行间隔时间
                      val time: Long = 0, //任务执行时长
                      val unit: TimeUnit = TimeUnit.SECONDS, //时间单位
                      val isRepeat: Boolean = false, //是否重复执行(false:不重复执行,有任务正在执行的话，不在开启新的任务，继续执行已存在的任务 true:重复执行，有任务正在执行的话，将再开启新的任务)
                      val isReStart: Boolean = false, //是否重新执行(isRepeat = false 时生效  true:重新执行(未执行完的任务 重新执行)
                      val isResumeStart: Boolean = false //是继续执行(未执行完的任务 继续执行)

)
```

**示例：**

### 1.task3()每次运行都将创建新的任务  **isRepeat = true**

```
@RecurringTask(taskCode = 3, initialDelay = 1,period = 1,time = 10,isRepeat = true, unit = TimeUnit.SECONDS, threadType = TaskThread.IO_THREAD)
fun task3(){
    Log.d("lance", "task 3 任务当前线程 ：${Thread.currentThread().name}")
}
```

结果：

```bash
 com.kiosoft2.testdemo.activity.TestActivity05  RecurringTask  f208f5f1-a18e-41d0-a71f-041639b24463   3  创建新任务
taskCode ：3
task 3 任务当前线程 ：RxCachedThreadScheduler-9
 com.kiosoft2.testdemo.activity.TestActivity05  RecurringTask  3c046cc7-637c-4519-ad99-78d439b27ef7   3  创建新任务
taskCode ：3
task 3 任务当前线程 ：RxCachedThreadScheduler-9
task 3 任务当前线程 ：RxCachedThreadScheduler-10
task 3 任务当前线程 ：RxCachedThreadScheduler-9
task 3 任务当前线程 ：RxCachedThreadScheduler-10

onTaskComplete 3
 com.kiosoft2.testdemo.activity.TestActivity05  RecurringTask  f208f5f1-a18e-41d0-a71f-041639b24463   3  完成
task 3 任务当前线程 ：RxCachedThreadScheduler-10
task 3 任务当前线程 ：RxCachedThreadScheduler-10
onTaskComplete 3
 com.kiosoft2.testdemo.activity.TestActivity05  RecurringTask  3c046cc7-637c-4519-ad99-78d439b27ef7   3  完成
```

### 2.task3()任务已存在将不再创建新的任务，只运行已存在的任务 **isRepeat = false**

```kotlin
@RecurringTask(taskCode = 3, initialDelay = 1,period = 1,time = 10,isRepeat = false, unit = TimeUnit.SECONDS, threadType = TaskThread.IO_THREAD)
fun task3(){
    Log.d("lance", "task 3 任务当前线程 ：${Thread.currentThread().name}")
}
```

结果：

```
com.kiosoft2.testdemo.activity.TestActivity05  RecurringTask  5f616e67-2202-4f86-b709-de2f1c395354   3  创建新任务
askCode ：3
ask 3 任务当前线程 ：RxCachedThreadScheduler-1
com.kiosoft2.testdemo.activity.TestActivity05  com.kiosoft2.common.task.annotions.RecurringTask   5f616e67-2202-4f86-b709-de2f1c395354   3  正在执行中
前任务已存在正在执行的任务，  RecurringTask  3  不再执行
ask 3 任务当前线程 ：RxCachedThreadScheduler-1
com.kiosoft2.testdemo.activity.TestActivity05  com.kiosoft2.common.task.annotions.RecurringTask   5f616e67-2202-4f86-b709-de2f1c395354   3  正在执行中
前任务已存在正在执行的任务，  RecurringTask  3  不再执行
ask 3 任务当前线程 ：RxCachedThreadScheduler-1
ask 3 任务当前线程 ：RxCachedThreadScheduler-1
ask 3 任务当前线程 ：RxCachedThreadScheduler-1
ask 3 任务当前线程 ：RxCachedThreadScheduler-1
nTaskComplete 3
com.kiosoft2.testdemo.activity.TestActivity05  RecurringTask  5f616e67-2202-4f86-b709-de2f1c395354   3  完成
```

### 3.停止已存在的任务(taskCode相同 && RecurringTask::class.java),重新创建新的任务，**停止已存在的任务采用的是中断信号的线程安全方式，不会立即停止任务具有延时性**  **isRepeat = false, isReStart = true**

```kotlin
@RecurringTask(taskCode = 3, initialDelay = 1,period = 1,time = 10,isRepeat = false, isReStart = true, unit = TimeUnit.SECONDS, threadType = TaskThread.IO_THREAD)
fun task3(){
    Log.d("lance", "task 3 任务当前线程 ：${Thread.currentThread().name}")
}
```

结果： 

```
com.kiosoft2.testdemo.activity.TestActivity05  RecurringTask  ce7a7f13-6926-46b0-b3db-8f37b8febbc1   3  创建新任务
taskCode ：3
task 3 任务当前线程 ：RxCachedThreadScheduler-1
 com.kiosoft2.testdemo.activity.TestActivity05  com.kiosoft2.common.task.annotions.RecurringTask   ce7a7f13-6926-46b0-b3db-8f37b8febbc1   3  正在执行中
 com.kiosoft2.testdemo.activity.TestActivity05  com.kiosoft2.common.task.annotions.RecurringTask    ce7a7f13-6926-46b0-b3db-8f37b8febbc1   3   关闭
 com.kiosoft2.testdemo.activity.TestActivity05  RecurringTask  da1c73fc-8d3a-46ad-b67e-a313cd761c6a   3  创建新任务
taskCode ：3
task 3 任务当前线程 ：RxCachedThreadScheduler-1
task 3 任务当前线程 ：RxCachedThreadScheduler-1
task 3 任务当前线程 ：RxCachedThreadScheduler-1
task 3 任务当前线程 ：RxCachedThreadScheduler-1
task 3 任务当前线程 ：RxCachedThreadScheduler-1
task 3 任务当前线程 ：RxCachedThreadScheduler-1
task 3 任务当前线程 ：RxCachedThreadScheduler-1
task 3 任务当前线程 ：RxCachedThreadScheduler-1
onTaskComplete 3
 com.kiosoft2.testdemo.activity.TestActivity05  RecurringTask  da1c73fc-8d3a-46ad-b67e-a313cd761c6a   3  完成
```

#### **Activity / Fragment 关联生命周期，触发ON_DESTROY 时 自动关闭正在执行的任务**

```
com.kiosoft2.testdemo.activity.TestActivity05  RecurringTask  167d051f-28dc-4ea5-b450-26e5ea589de8   3  创建新任务
askCode ：3
oinPoint.`this` is LifecycleOwner true
oinPoint.this.name com.kiosoft2.testdemo.activity.TestActivity05
ifecycleOwner  com.kiosoft2.testdemo.activity.TestActivity05  绑定添加
askLifecycleManager 收到消息  com.kiosoft2.testdemo.activity.TestActivity05   ON_CREATE
askLifecycleManager 收到消息   com.kiosoft2.testdemo.activity.TestActivity05   ON_START
askLifecycleManager 收到消息   com.kiosoft2.testdemo.activity.TestActivity05   ON_RESUME
ask 3 任务当前线程 ：RxCachedThreadScheduler-1
ask 3 任务当前线程 ：RxCachedThreadScheduler-1
ask 3 任务当前线程 ：RxCachedThreadScheduler-1
askLifecycleManager 收到消息   com.kiosoft2.testdemo.activity.TestActivity05   ON_PAUSE
askLifecycleManager 收到消息   com.kiosoft2.testdemo.activity.TestActivity05   ON_STOP
askLifecycleManager 收到消息   com.kiosoft2.testdemo.activity.TestActivity05   ON_DESTROY
com.kiosoft2.testdemo.activity.TestActivity05   167d051f-28dc-4ea5-b450-26e5ea589de8   3  关闭
TestActivity05 onDestroy 页面关闭
```

#### **非Activity / Fragment 自定义类，  自动关联生命周期 实现ILifecycleOwner**

```kotlin
class TestLifcycle: ILifecycleOwner() {
    override val lifecycle: Lifecycle
        get() = super.lifecycleRegistry
    @RecurringTask(taskCode = 1, initialDelay = 1,period = 1,time = 10,isRepeat = false, isReStart = true, unit = TimeUnit.SECONDS, threadType = TaskThread.IO_THREAD)
    fun task(){
        Log.d("lance", "自定义类任务执行 ：${Thread.currentThread().name}")
    }
}
```

```kotlin
testLifcycle.onDestroy() //未执行完的任务 将会自动关闭不在执行
```

结果：  

```
n1.b  RecurringTask  3dba408e-3d61-4aa2-8447-234cf9a40ac5   1  创建新任务
 TaskLifecycleManager 收到消息  n1.b   ON_CREATE
 自定义类任务执行 ：RxCachedThreadScheduler-1
 TaskLifecycleManager 收到消息   n1.b   ON_DESTROY
  n1.b   3dba408e-3d61-4aa2-8447-234cf9a40ac5   1  关闭
```

### **任务结束回调监听（@TaskComplete）**

```
@TaskComplete
override fun onTaskComplete(taskInfo: TaskInfo) {
    Log.d("lance", "onTaskComplete ${taskInfo.taskCode}")
}
```

### **自行管理任务@TaskBindDisposable** 获取任务对象，或通过任务服务进行管理

```kotlin
@TaskBindDisposable
override fun bindDisposable(taskInfo: TaskInfo) {
    Log.d("lance", "taskCode ：${taskInfo.taskCode}")
    if (taskInfo.taskCode == 1){//taskcode 和@RecurringTask(taskCode = 3 一致
        task1 = taskInfo
    }else if (taskInfo.taskCode == 2){
        task2s = taskInfo
    }
}
```

```kotlin
TaskInfo：
  var taskId:String = "",//任务id 自动生成唯一值 isRepeat = true 时 创建的taskCode相同 taskId会创建多个  taskId是任务的唯一值
  var taskCode:Int = 0,//任务code 和@RecurringTask(taskCode = 3 一致
var endTimeMillis:Long = 0,//任务结束时间时间戳
/**
     * 取消任务
     */
    fun cancleTask()
    /**
     * 任务是否执行中
     */
    fun isRunning()
```

### 任务执行体线程：

**TaskThread.IO_THREAD子线程  TaskThread.MAIN_THREAD主线程** **不设置threadType 默认当前线程**

```kotlin
@RecurringTask(taskCode = 5, initialDelay = 1,period = 1,time = 10,isRepeat = true, unit = TimeUnit.SECONDS, threadType = TaskThread.IO_THREAD)
fun task5(){
    Log.d("lance", "task 5 任务当前线程 ：${Thread.currentThread().name}")
}
@RecurringTask(taskCode = 6, initialDelay = 1,period = 1,time = 10,isRepeat = true, unit = TimeUnit.SECONDS, threadType = TaskThread.MAIN_THREAD)
fun task6(){
    Log.d("lance", "task 6 任务当前线程 ：${Thread.currentThread().name}")
}
```

```
task 5 任务当前线程 ：RxCachedThreadScheduler-1
task 6 任务当前线程 ：main
```

## @DelayedTask 延时任务  其他功能和@**RecurringTask**一致

```kotlin
@DelayedTask(taskCode = 3,initialDelay = 20, unit = TimeUnit.SECONDS, threadType = TaskThread.IO_THREAD)
fun delayedTask(){
    Log.d("lance", "延时执行任务 ：${Thread.currentThread().name}")
}
@DelayedTask(taskCode = 4,initialDelay = 10, unit = TimeUnit.SECONDS, threadType = TaskThread.MAIN_THREAD)
fun delayedTask2(){
    Log.d("lance", "延时执行任务 ：${Thread.currentThread().name}")
}
```

```
taskCode ：4
延时执行任务 ：main
 com.kiosoft2.testdemo.activity.TestActivity05  DelayedTask 0e194df7-d542-48d0-946d-a094152d0c34   4  完成
onTaskComplete 4
延时执行任务 ：RxCachedThreadScheduler-1
 com.kiosoft2.testdemo.activity.TestActivity05  DelayedTask eecfa625-ef8c-4b90-91d7-5159da53be2d   3  完成
onTaskComplete 3
```

## 任务缓存

### **生命周期绑定：**

### Application中进行注册绑定将自动绑定Activity / Fragment 的生命周期，当oncreate执行后自动执行缓存的定时任务

```kotlin
TaskManager.registerActivityLifecycleCallbacks(this)
```



## 需要缓存任务并在页面再次创建的时候继续执行上次未执行完的任务（实现TaskReLoadCallback接口）

## 示例

### Activity / Fragment  （isCacheResumeStart = true）

```kotlin
 mBinding.btnPost.setSafeOnClickListener {
                task3();}
@RecurringTask(taskCode = 3, initialDelay = 1,period = 1,time = 200,isRepeat = false, isReStart = true, unit = TimeUnit.SECONDS, threadType = TaskThread.IO_THREAD, isCacheResumeStart = true)
fun task3(str: String = "123",age:Int = 12,testModel: TestModel = TestModel()){
     //业务逻辑
    Log.d("lance", "task 3 任务当前线程 ：${Thread.currentThread().name}")
}
```

实现TaskReLoadCallback接口 重写onReLoadStart方法，当页面再次创建后自动回调onReLoadStart方法内容

```kotlin
override fun onReLoadStart(taskInfo: TaskInfo) {
    // //业务逻辑
    Log.d("lance", "onReLoadStart 任务当前线程 ：${Thread.currentThread().name}  ${taskInfo.taskId}")
}
```

```
onActivityResumed:    com.kiosoft2.testdemo.activity.TestActivity05 : 
 com.kiosoft2.testdemo.activity.TestActivity05  RecurringTask  3c450d2c-43f7-4c76-a86f-2c9a093b76eb   3  创建新任务
taskCode ：3
TaskLifecycleManager 收到消息  com.kiosoft2.testdemo.activity.TestActivity05   ON_CREATE
TaskLifecycleManager 收到消息   com.kiosoft2.testdemo.activity.TestActivity05   ON_START
TaskLifecycleManager 收到消息   com.kiosoft2.testdemo.activity.TestActivity05   ON_RESUME
task 3 任务当前线程 ：RxCachedThreadScheduler-1
task 3 任务当前线程 ：RxCachedThreadScheduler-1
task 3 任务当前线程 ：RxCachedThreadScheduler-1
task 3 任务当前线程 ：RxCachedThreadScheduler-1
TaskLifecycleManager 收到消息   com.kiosoft2.testdemo.activity.TestActivity05   ON_PAUSE
onActivityPaused:    com.kiosoft2.testdemo.activity.TestActivity05 : 
TaskLifecycleManager 收到消息   com.kiosoft2.testdemo.activity.TestActivity05   ON_STOP
onActivityStopped:    com.kiosoft2.testdemo.activity.TestActivity05 : 
onActivitySaveInstanceState:    com.kiosoft2.testdemo.activity.TestActivity05 : 
task 3 任务当前线程 ：RxCachedThreadScheduler-1
TaskLifecycleManager 收到消息   com.kiosoft2.testdemo.activity.TestActivity05   ON_DESTROY
 com.kiosoft2.testdemo.activity.TestActivity05   3c450d2c-43f7-4c76-a86f-2c9a093b76eb   3  关闭
onActivityDestroyed:    com.kiosoft2.testdemo.activity.TestActivity05 : 
 TestActivity05 onDestroy 页面关闭

----------- PROCESS ENDED (27530) for package com.kiosoft2.testdemo ----------------------------
----------- PROCESS STARTED (27626) for package com.kiosoft2.testdemo ----------------------------
onActivityCreated:    com.kiosoft2.testdemo.activity.TestActivity05 : 
重新执行 时长:    191 
缓存任务开始重新加载执行，  com.kiosoft2.common.task.annotions.RecurringTask 3c450d2c-43f7-4c76-a86f-2c9a093b76eb  3  重新加载执行
taskCode ：3
TaskLifecycleManager 收到消息  com.kiosoft2.testdemo.activity.TestActivity05   ON_CREATE
onActivityStarted:    com.kiosoft2.testdemo.activity.TestActivity05 : 
TaskLifecycleManager 收到消息   com.kiosoft2.testdemo.activity.TestActivity05   ON_START
onActivityResumed:    com.kiosoft2.testdemo.activity.TestActivity05 : 
TaskLifecycleManager 收到消息   com.kiosoft2.testdemo.activity.TestActivity05   ON_RESUME
onReLoadStart 任务当前线程 ：RxCachedThreadScheduler-1  3c450d2c-43f7-4c76-a86f-2c9a093b76eb
onReLoadStart 任务当前线程 ：RxCachedThreadScheduler-1  3c450d2c-43f7-4c76-a86f-2c9a093b76eb
onReLoadStart 任务当前线程 ：RxCachedThreadScheduler-1  3c450d2c-43f7-4c76-a86f-2c9a093b76eb
onReLoadStart 任务当前线程 ：RxCachedThreadScheduler-1  3c450d2c-43f7-4c76-a86f-2c9a093b76eb

onReLoadStart 任务当前线程 ：RxCachedThreadScheduler-1  3c450d2c-43f7-4c76-a86f-2c9a093b76eb
onTaskComplete 3
 com.kiosoft2.testdemo.activity.TestActivity05  RecurringTask  3c450d2c-43f7-4c76-a86f-2c9a093b76eb   3  完成
```

### 自定义类

```kotlin
class TestLifcycle: ILifecycleOwner() , TaskReLoadCallback {
    override val lifecycle: Lifecycle
        get() = super.lifecycleRegistry

    /**
     * 任务缓存服务
     */
    var taskCacheService: TaskCacheService? = lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        ServiceLoad.load(TaskCacheService::class.java)
    }.value
    init {
        Log.d("lance", "taskCacheService == null ：${taskCacheService == null}")
        taskCacheService?.reStartTask(this)
    }

    @RecurringTask(isCacheResumeStart = true,taskCode = 1, initialDelay = 1,period = 1,time = 200,isRepeat = false, isReStart = true, unit = TimeUnit.SECONDS, threadType = TaskThread.IO_THREAD)
    fun task(){
        taskRun()
    }

    override fun onReLoadStart(taskInfo: TaskInfo) {
        Log.d("lance", "业务内容 回调执行 ：${taskInfo.taskId}")
        taskRun()
    }

    private fun taskRun() {
        Log.d("lance", "业务内容 ")
    }

    override fun bindDisposable(taskInfo: TaskInfo) {
        Log.d("lance", "创建任务绑定 ：${taskInfo.taskId}")
    }

    override fun onTaskComplete(taskInfo: TaskInfo) {
        Log.d("lance", "任务完成 ：${taskInfo.taskId}")
    }
}
```

****

```
onActivityCreated:    com.kiosoft2.testdemo.activity.TestActivity05 :** 
**taskCacheService == null ：false**
**onActivityStarted:    com.kiosoft2.testdemo.activity.TestActivity05 :** 
**onActivityResumed:    com.kiosoft2.testdemo.activity.TestActivity05 :** 
 **y1.b  RecurringTask  7c1667fa-0e6c-448c-9c59-d3b7273a8496   1  创建新任务**
**TaskLifecycleManager 收到消息  y1.b   ON_CREATE**
**业务内容** 
**业务内容** 
**业务内容** 
**业务内容** 
**onActivityPaused:    com.kiosoft2.testdemo.activity.TestActivity05 :** 
**onActivityStopped:    com.kiosoft2.testdemo.activity.TestActivity05 :** 
**onActivitySaveInstanceState:    com.kiosoft2.testdemo.activity.TestActivity05 :** 
**业务内容** 
**onActivityDestroyed:    com.kiosoft2.testdemo.activity.TestActivity05 :** 
 **TestActivity05 onDestroy 页面关闭**
**----------- PROCESS ENDED (11339) for package com.kiosoft2.testdemo ----------------------------**
**----------- PROCESS STARTED (11407) for package com.kiosoft2.testdemo ----------------------------**
**onActivityCreated:    com.kiosoft2.testdemo.activity.TestActivity05 :** 
**taskCacheService == null ：false**
**重新执行 时长:    ${it.time}** 
**缓存任务开始重新加载执行，  com.kiosoft2.common.task.annotions.RecurringTask 7c1667fa-0e6c-448c-9c59-d3b7273a8496  1  重新加载执行**
**TaskLifecycleManager 收到消息  y1.b   ON_CREATE**
**onActivityStarted:    com.kiosoft2.testdemo.activity.TestActivity05 :** 
**onActivityResumed:    com.kiosoft2.testdemo.activity.TestActivity05 :** 
**业务内容 回调执行 ：7c1667fa-0e6c-448c-9c59-d3b7273a8496**
**业务内容** 
**业务内容 回调执行 ：7c1667fa-0e6c-448c-9c59-d3b7273a8496**
**业务内容** 
**业务内容 回调执行 ：7c1667fa-0e6c-448c-9c59-d3b7273a8496**
业务内容** 
```

## 任务服务

```kotlin
interface TaskCacheService {
    /**
     * 重新加载任务:缓存的任务重新加载执行（执行完成/已取消的任务不可重启）
     */
    fun reStartTask(obj:Any)
    /**
     * 重新加载任务::缓存的任务重新加载执行（执行完成/已取消的任务不可重启）
     */
    fun reStartTask(obj:Any,taskId:String)

    /**
     * 取消/删除任务（正在运行的任务会中断 并移除，缓存的任务会直接移除）
     */
    fun cancelTask(obj:Any)
    /**
     * 取消/删除任务（正在运行的任务会中断 并移除，缓存的任务会直接移除）
     */
    fun cancelTask(obj:Any,taskId:String)
}
```

### 任务服务实例化

         * ```kotlin
                  */ /**
        
               * 任务缓存服务
        
             ​        var taskCacheService: TaskCacheService? = lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
             ServiceLoad.load(TaskCacheService::class.java)
             ​        }.value
        ```
        
        

### 示例

```kotlin

        testLifcycle = TestLifcycle()
        mBinding.btnNext.setSafeOnClickListener {
            startActivity(Intent(this@TestActivity05,TestActivity03::class.java))
        }
        mBinding.btnStop.setSafeOnClickListener {
            taskCacheService?.cancelTask(testLifcycle)
        }
            mBinding.btnPost.setSafeOnClickListener {
                testLifcycle.task()
            }
```

```
业务内容 
onActivityDestroyed:    com.kiosoft2.testdemo.activity.
 TestActivity05 onDestroy 页面关闭
----------- PROCESS ENDED (11407) for package com.kioso
----------- PROCESS STARTED (12648) for package com.kio
onActivityCreated:    com.kiosoft2.testdemo.activity.Te
taskCacheService == null ：false
重新执行 时长:    ${it.time} 
缓存任务开始重新加载执行，  com.kiosoft2.common.task.annotions.RecurringTask 009f4a8e-772d-4eab-bdc8-291c986ce321  1  重新加载执行
TaskLifecycleManager 收到消息  y1.b   ON_CREATE
onActivityStarted:    com.kiosoft2.testdemo.activity.Te
onActivityResumed:    com.kiosoft2.testdemo.activity.Te
业务内容 回调执行 ：009f4a8e-772d-4eab-bdc8-291c986ce321
业务内容 
业务内容 回调执行 ：009f4a8e-772d-4eab-bdc8-291c986ce321
业务内容 
业务内容 回调执行 ：009f4a8e-772d-4eab-bdc8-291c986ce321

TaskLifecycleManager 收到消息   y1.b   ON_DESTROY
 y1.b   009f4a8e-772d-4eab-bdc8-291c986ce321   1  关闭
```



## 注意！注意！注意！

```kotlin
缓存任务是和class对象绑定的，即：

var m1 = TestLifcycle()

var m2 = TestLifcycle()

获取的缓存任务列表是相同的
```

# 页面抖动

### 示例

kotlin

```kotlin
mBinding.btnNext.setSafeOnClickListener {
    startActivity(Intent(this@TestActivity02, TestActivity03::class.java))
    finish()
}
```

```java
ClickExe.setSafeOnClickListener(findViewById(R.id.btn_next),new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Log.d("lance", "onClick: ");
        task();
        task2();
        delayedTask();
    }});
}
```

### 方法注解 @SingleTime 默认800毫秒

```kotlin
 mBinding.btnStop.setOnClickListener {
            testSingleTime()
        }
   @SingleTime
    fun testSingleTime(){
        Log.d("lance", "testSingleTime ：${Thread.currentThread().name}")
    }
```

```
11:36:58.790  D  testSingleTime ：main
11:36:59.595  D  testSingleTime ：main
11:37:00.404  D  testSingleTime ：main
11:37:01.259  D  testSingleTime ：main
11:37:02.099  D  testSingleTime ：main
11:37:02.918  D  testSingleTime ：main
```



# 线程

### 子线程执行 @RunInIO

```kotlin
@RunInIO
 fun onPost(num:Int){
     Log.d("lance", "onPost ${num}")
     Log.d("lance", "thread ${Thread.currentThread().name}")
    onSend(num)
 }
```

### 主线程执行 @RunInMainThread

```kotlin
@RunInMainThread
fun onSend(num:Int){
    Log.d("lance", "onSend ${num}   ${Thread.currentThread().name}")
}
```

## 混淆配置

```kotlin
# 保持自定义库的所有类和成员不被混淆
-keep class com.kiosoft2.common.** { *; }
-keep class com.kiosoft2.common.**.** { *; }

# 如果自定义库中有一些特定的注解需要保持不被混淆，可以添加类似的规则
-keep @com.kiosoft2.common.thead.annotions.** class * {*;}
-keep @com.kiosoft2.common.click.annotions.** class * {*;}
-keep @com.kiosoft2.common.task.annotions.** class * {*;}

# 如果自定义库中使用了一些特定的接口，也需要保持不被混淆
-keep interface com.kiosoft2.common.task.interfaces.** { *; }

# 如果自定义库包含一些特定的枚举类型，同样需要保持不被混淆
-keepclassmembers enum com.kiosoft2.common.task.annotions.** { *; }
```

