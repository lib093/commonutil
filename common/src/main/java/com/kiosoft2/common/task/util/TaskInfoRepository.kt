package com.kiosoft2.common.task.util

import android.util.Log
import com.kiosoft2.common.cache.db.dao.TaskInfoDao
import com.kiosoft2.common.task.model.TaskInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaskInfoRepository(var taskInfoDao: TaskInfoDao) {
    suspend fun insertTaskInfo(taskInfo: TaskInfo) {
        withContext(Dispatchers.IO){
            taskInfoDao.insertTaskInfo(taskInfo)
        }
    }

    suspend fun insertTaskInfoList(taskInfoList: MutableList<TaskInfo>) {
        withContext(Dispatchers.IO){
        taskInfoDao.insertTaskInfoList(taskInfoList)
        }
    }
    suspend fun insertOrUpdateTaskInfo(taskInfo: TaskInfo) {
        withContext(Dispatchers.IO){
            taskInfoDao.insertOrUpdateTaskInfo(taskInfo)
        }
    }

    suspend fun updateTaskInfo(taskInfo: TaskInfo) {
        withContext(Dispatchers.IO) {
            taskInfoDao.updateTaskInfo(taskInfo)
        }
    }
    suspend fun getTaskInfoByTaskId(taskId: String): TaskInfo {
        return withContext(Dispatchers.IO) {
            taskInfoDao.getTaskInfoByTaskId(taskId)

        }
    }

    suspend fun updateTaskInfoList(taskInfoList: MutableList<TaskInfo>) {
        withContext(Dispatchers.IO) {
            taskInfoDao.updateTaskInfoList(taskInfoList)
        }
    }

    suspend fun getTaskList(): MutableList<TaskInfo> {
        return withContext(Dispatchers.IO) {
             taskInfoDao.getTaskList()
        }
    }

    suspend fun getCacheTaskListByOwerClassName(owerClassName: String): MutableList<TaskInfo> {
        return  withContext(Dispatchers.IO) {
            taskInfoDao.getCacheTaskListByOwerClassName(owerClassName)
        }
    }
}