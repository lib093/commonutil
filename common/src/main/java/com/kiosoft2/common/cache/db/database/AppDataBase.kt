package com.kiosoft2.common.cache.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kiosoft2.common.cache.db.dao.TaskInfoDao
import com.kiosoft2.common.task.model.TaskInfo

@Database(entities = [TaskInfo::class], version = 1, exportSchema = false)
abstract class AppDataBase: RoomDatabase() {
    abstract fun getTaskInfoDao(): TaskInfoDao
}