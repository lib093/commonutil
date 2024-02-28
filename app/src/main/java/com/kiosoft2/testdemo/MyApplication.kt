package com.kiosoft2.testdemo

import android.app.Application
import com.kiosoft2.common.cache.CacheManager
import com.kiosoft2.common.task.util.TaskManager.registerActivityLifecycleCallbacks
import com.tencent.mmkv.MMKV
import com.ubix.kiosoft2.db.DBOperator

class MyApplication:Application() {
     companion object{
         lateinit var myApplication:MyApplication
         fun getInstance():MyApplication{
             return myApplication
         }
     }

    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
        myApplication = this
        CacheManager.init(this)
        DBOperator.init(this)
    }
}