package com.kiosoft2.common.task.model

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.kiosoft2.common.task.annotions.TaskThread
import io.reactivex.rxjava3.disposables.Disposable
import java.io.Serializable
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit
@Entity(tableName = "task_info")
data class TaskInfo(
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0,
    @Expose
    @ColumnInfo
    var owerClassName:String,
    @Ignore
    var annotationClass: Class<out Annotation>? = null,
    @Expose
    @ColumnInfo
    var annotationClassName: String? = null,
    @Ignore
    var lifecycleOwner: LifecycleOwner? = null,
    @Expose
    @ColumnInfo
    var taskId:String = "",//任务id 自动生成唯一值
    @Expose
    @ColumnInfo
    var taskCode:Int = 0,//任务code
    @Expose
    @ColumnInfo
    var endTimeMillis:Long = 0,//任务结束时间时间戳
    @Ignore
    private var weakReference: WeakReference<Disposable>? = null,//任务订阅者\
    @Expose
    @ColumnInfo
    var isReStart: Boolean = false, //是否 之前关闭的任务在启动新的
    @Expose
    @ColumnInfo
    var isCacheResumeStart: Boolean = false,//是继续执行(未执行完的任务进行持久化后 继续执行)
    @Expose
    @ColumnInfo
    var threadType: TaskThread = TaskThread.CURR_THREAD,//线程类型\
    @Expose
    @ColumnInfo
    var initialDelay: Long = 0, //初始延迟时间
    @Expose
    @ColumnInfo
    var period: Long = 0, //任务执行间隔时间
    @Expose
    @ColumnInfo
    var time: Long = 0, //任务执行时长
    @Expose
    @ColumnInfo
    var unit: TimeUnit = TimeUnit.SECONDS, //时间单位
    @Expose
    @ColumnInfo
    var isRepeat: Boolean = false, //是否重复执行(false:不重复执行,有任务正在执行的话，不在开启新的任务，继续执行已存在的任务 true:重复执行，有任务正在执行的话，将再开启新的任务)
):Serializable{
    constructor() : this(0,"",null,null,null,
        "",0,0,null,false,false,TaskThread.MAIN_THREAD,0,0,
        0,TimeUnit.SECONDS,false)

    /**
     * 设置任务订阅者
     */
    fun setDisposable(disposable: Disposable){
        weakReference = WeakReference(disposable)
    }
    /**
     * 取消任务
     */
    fun cancleTask(){
        if (weakReference?.get()?.isDisposed == false) {
            weakReference?.get()?.dispose()
        }
    }

    /**
     * 任务是否执行中
     */
    fun isRunning():Boolean{
        if (weakReference?.get() == null){
            Log.d(
                "lance",
                "${weakReference?.get() == null}  taskId ${this.taskId} weakReference?.get() == null"
            )
        }
        return weakReference?.get()?.isDisposed == false
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TaskInfo

        if (taskId != other.taskId) return false

        return true
    }

    override fun hashCode(): Int {
        return taskId.hashCode()
    }


}
