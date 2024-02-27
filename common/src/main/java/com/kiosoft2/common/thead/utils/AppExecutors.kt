package com.kiosoft2.common.thead.utils
import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

/**
 * 全局线程池任务， 给整个项目 用 网络线程池，磁盘线程池，主线程 都很方便 的使用此类即可
 * 整个应用程序的全局执行器池。
 * 这样的任务分组可以避免任务饥饿的影响（例如，磁盘读取不会在webservice请求之后等待）。
 */
class AppExecutors  constructor(
     val diskIO: Executor, // 磁盘任务IO
     val networkIO: Executor, // 网络任务IO
     val mainThread: Executor,// 主线程任务
     val timeworkIO: ScheduledExecutorService // 周期任务
) {
    // 只是配合单例模式用的 Holder 而已
     object InstanceHolder {
         val instance = AppExecutors(
            DiskIOThreadExecutor(),  // 磁盘任务IO 异步的
            Executors.newFixedThreadPool(THREAD_COUNT),  // 网络任务IO 异步的
            MainThreadExecutor() ,// 主线程
             Executors.newScheduledThreadPool(THREAD_COUNT)
        )
    }

    // 真正对外暴漏出去被调用的函数 （磁盘线程池 异步）
    fun diskIO(): Executor {
        return diskIO
    }

    // 真正对外暴漏出去被调用的函数 （网络线程池 异步）
    fun networkIO(): Executor {
        return networkIO
    }

    // 真正对外暴漏出去被调用的函数 （主线程 非异步）
    fun mainThread(): Executor {
        return mainThread
    }
    fun timeworkIO(): Executor {
        return timeworkIO
    }

    // 主线程任务
     class MainThreadExecutor : Executor {
        // 从异步线程 到 UI线程  Looper.getMainLooper()
         val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }

    // 磁盘任务IO
    internal class DiskIOThreadExecutor : Executor {
         val mDiskIO: Executor
        override fun execute(command: Runnable) {
            mDiskIO.execute(command)
        }

        init {
            mDiskIO = Executors.newSingleThreadExecutor()
        }
    }

    public companion object {
         const val THREAD_COUNT = 5

        // 单例模式
       public val instance: AppExecutors
            get() = InstanceHolder.instance
    }
}