package com.kiosoft2.common.autoservice.service

interface TaskService {
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