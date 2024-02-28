package com.kiosoft2.common.cache

import android.app.Application
import android.util.Log
import androidx.annotation.Keep
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.kiosoft2.common.cache.configs.TASK_CACHE_KEY
import com.kiosoft2.common.cache.db.dao.TaskInfoDao
import com.kiosoft2.common.cache.db.database.AppDataBase
import com.kiosoft2.common.task.model.TaskInfo
import com.kiosoft2.common.task.util.TaskInfoRepository
import com.tencent.mmkv.MMKV
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.concurrent.CopyOnWriteArrayList
@Keep
object CacheManager {
    lateinit var mmkv:MMKV
    var gson = GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation() // 仅包含带有 @Expose 注解的字段
        .create();
    lateinit var db:AppDataBase
    lateinit var taskInfoDao: TaskInfoDao
    lateinit var taskInfoRepository: TaskInfoRepository
    fun init(application: Application){
        MMKV.initialize(application)
        mmkv = MMKV.defaultMMKV()
        db = Room.databaseBuilder(application, AppDataBase::class.java, "CPM_DATABASE").build()
        taskInfoDao = db.getTaskInfoDao()
        taskInfoRepository = TaskInfoRepository(taskInfoDao)
    }
    fun cacheTaskList(taskList:CopyOnWriteArrayList<TaskInfo>) {
        var list: CopyOnWriteArrayList<TaskInfo>? = null
        var listStr = mmkv.decodeString(TASK_CACHE_KEY)
        if (listStr == null) {
            list = CopyOnWriteArrayList()
        }else{
            list = gson.fromJson(listStr, object : TypeToken<List<TaskInfo>>() {}.type)
        }
        if (list!!.size > 0) {
            list.removeAll(taskList)
        }
        list!!.addAll(taskList)
        mmkv.encode(TASK_CACHE_KEY, gson.toJson(list))
    }
    fun refreshCacheTaskList(taskList:CopyOnWriteArrayList<TaskInfo>) {
        mmkv.encode(TASK_CACHE_KEY, gson.toJson(taskList))
    }
    fun cacheTask(task:TaskInfo) {
//        var list: MutableList<TaskInfo>? = null
//        var listStr = mmkv.decodeString(TASK_CACHE_KEY)
//        if (listStr == null) {
//            list = mutableListOf()
//        }else{
//            list = gson.fromJson(listStr, object : TypeToken<List<TaskInfo>>() {}.type)
//        }
//        if (list!!.size > 0) {
//            list.remove(task)
//        }
//        list!!.add(task)
//        mmkv.encode(TASK_CACHE_KEY, gson.toJson(list))
        GlobalScope.launch(Dispatchers.IO){
            runBlocking {
            taskInfoRepository.insertTaskInfo(task)
            }
        }

    }
    fun getCacheTaskList():MutableList<TaskInfo>{
        var listStr = mmkv.decodeString(TASK_CACHE_KEY)
        if (listStr == null) {
            return mutableListOf()
        }
        return gson.fromJson(listStr, object : TypeToken<List<TaskInfo>>() {}.type)
    }

    /**
     * 根据owerClassName 获取缓存的任务列表
     */
    suspend fun getCacheTaskListByOwerClassName(owerClassName:String): MutableList<TaskInfo> {
//        var listStr = mmkv.decodeString(TASK_CACHE_KEY)
//        if (listStr == null) {
//            return mutableListOf()
//        }
//        var list = gson.fromJson(listStr, object : TypeToken<List<TaskInfo>>() {}.type) as MutableList<TaskInfo>
//        return list.filter { it.owerClassName == owerClassName }.toMutableList()
        return withContext(Dispatchers.IO){
           taskInfoDao.getCacheTaskListByOwerClassName(owerClassName)
        }
    }
    /**
     * 根据owerClassName & taskId 获取缓存的任务列表
     */
    suspend fun getCacheTaskListByOwerClassNameAndTaskId(owerClassName:String,taskId:String):TaskInfo?{
//        var listStr = mmkv.decodeString(TASK_CACHE_KEY)
//        if (listStr == null) {
//            return null
//        }
//        var list = gson.fromJson(listStr, object : TypeToken<List<TaskInfo>>() {}.type) as MutableList<TaskInfo>
//        return list.find { it.owerClassName == owerClassName && it.taskId == taskId }
        return withContext(Dispatchers.IO){taskInfoDao.getCacheTaskListByOwerClassNameAndTaskId(owerClassName,taskId)}
    }

    /**
     * 移除持久化的任务
     */
    suspend fun removeCacheTask(task:TaskInfo){
//        var list: MutableList<TaskInfo>? = null
//        var listStr = mmkv.decodeString(TASK_CACHE_KEY)
//        if (listStr == null) {
//            return
//        }else{
//            list = gson.fromJson(listStr, object : TypeToken<List<TaskInfo>>() {}.type)
//        }
//        list!!.remove(task)
//        mmkv.encode(TASK_CACHE_KEY, gson.toJson(list))
        withContext(Dispatchers.IO){taskInfoDao.removeTaskInfoByTaskId(task.taskId)}
    }
    /**
     * 移除持久化的任务
     */
    fun removeCacheTaskList(taskList:MutableList<TaskInfo>){
        var list: MutableList<TaskInfo>? = null
        var listStr = mmkv.decodeString(TASK_CACHE_KEY)
        if (listStr == null) {
            return
        }else{
            list = gson.fromJson(listStr, object : TypeToken<List<TaskInfo>>() {}.type)
        }
        list!!.removeAll(taskList)
        mmkv.encode(TASK_CACHE_KEY, gson.toJson(list))
    }
}