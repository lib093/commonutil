package com.kiosoft2.common.cache.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kiosoft2.common.task.model.TaskInfo

@Dao
interface TaskInfoDao {
    /**
     * 缓存任务
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskInfo(taskInfo: TaskInfo)

    /**
     * 缓存任务列表
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskInfoList(taskInfoList:MutableList<TaskInfo>)

    /**
     * 更新任务
     */
    @Update
    suspend fun updateTaskInfo(taskInfo: TaskInfo)

    /**
     * 更新任务列表
     */
    @Update
    suspend fun updateTaskInfoList(taskInfoList:MutableList<TaskInfo>)
    /**
     * 插入或更新任务
     */
    suspend fun insertOrUpdateTaskInfo(taskInfo: TaskInfo){
        var cachetaskInfo = getTaskInfoByTaskId(taskInfo.taskId)
        if (cachetaskInfo == null) {
            insertTaskInfo(taskInfo)
        }else{
            taskInfo.id = cachetaskInfo.id
            updateTaskInfo(taskInfo)
        }
    }

    /**
     * 获取任务列表
     */
    @Query("SELECT * FROM task_info")
    suspend fun getTaskList():MutableList<TaskInfo>
    @Query("SELECT * FROM task_info WHERE taskId = :taskId")
    suspend fun getTaskInfoByTaskId(taskId:String):TaskInfo
    /**
     * 根据owerClassName 获取缓存的任务列表
     */
    @Query("SELECT * FROM task_info WHERE owerClassName = :owerClassName")
    suspend  fun getCacheTaskListByOwerClassName(owerClassName:String):MutableList<TaskInfo>

    /**
     * 根据owerClassName 和 taskId 获取缓存的任务列表
     */
    @Query("SELECT * FROM task_info WHERE owerClassName = :owerClassName AND taskId = :taskId")
    suspend fun getCacheTaskListByOwerClassNameAndTaskId(owerClassName:String,taskId:String):TaskInfo

    @Delete
    suspend  fun removeCacheTask(taskInfo:TaskInfo)
    @Delete
    suspend  fun removeCacheTaskList(taskInfoList:MutableList<TaskInfo>)
    @Query("DELETE FROM task_info WHERE owerClassName = :owerClassName")
    suspend  fun removeTaskInfoByOwerClassName(owerClassName:String)
    @Query("DELETE FROM task_info WHERE taskId = :taskId")
    suspend  fun removeTaskInfoByTaskId(taskId:String)
}