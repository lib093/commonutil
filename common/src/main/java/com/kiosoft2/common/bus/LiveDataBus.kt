package com.kiosoft2.common.bus

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class LiveDataBus {
    //存放订阅者
    private val bus: MutableMap<String, BusMutableLiveData<Any?>> by lazy {
        HashMap()
    }
    private fun LiveDataBus() {
    }

    companion object {
        private lateinit var liveDataBus: LiveDataBus
        fun getInstance(): LiveDataBus {
            if (!::liveDataBus.isInitialized) {
                synchronized(LiveDataBus::class.java) {
                    if (!::liveDataBus.isInitialized) {
                        liveDataBus = LiveDataBus()
                    }
                }
            }
            return liveDataBus
        }
    }
    fun remove(key: String) {
        if (bus!!.containsKey(key)) {
            bus!!.remove(key)
        }
    }
    //注册订阅者
    @Synchronized
    fun <T> with(key: String, type: Class<T>?): BusMutableLiveData<T?> {
        if (!bus!!.containsKey(key)) {
            bus!![key] = BusMutableLiveData()
        }
        return bus!![key] as BusMutableLiveData<T?>
    }

    //解决粘性事件
    class BusMutableLiveData<T> : MutableLiveData<T>() {
        override fun  observe(owner: LifecycleOwner, observer: Observer<in T>) {
            super.observe(owner, observer)
            hook(observer)
        }

        override fun observeForever(observer: Observer<in T>) {
            super.observeForever(observer)
            hook(observer)
        }

        private fun hook(observer: Observer<in T>) {
            try {
                //1.得到mLastVersion
                //获取到LivData的类中的mObservers对象
                val liveDataClass = LiveData::class.java
                val mObserversField = liveDataClass.getDeclaredField("mObservers")
                mObserversField.isAccessible = true
                //获取到这个成员变量的对象
                val mObserversObject =
                    mObserversField[this] //   private SafeIterableMap<Observer<? super T>, ObserverWrapper> mObservers = new SafeIterableMap<>();
                //得到map对象的class对象
                val mObserversClass: Class<*> = mObserversObject.javaClass // SafeIterableMap.class
                //获取到mObservers对象的get方法
                val get =
                    mObserversClass.getDeclaredMethod("get", Any::class.java) //Entry<K, V> get(K k)
                get.isAccessible = true
                //执行get方法
                val invokeEntry = get.invoke(mObserversObject, observer) //Entry<K, V>
                //取到entry中的value
                var observerWraper: Any? = null
                if (invokeEntry != null && invokeEntry is Map.Entry<*, *>) {
                    observerWraper = invokeEntry.value
                }
                if (observerWraper == null) {
                    throw NullPointerException("observerWraper is null")
                }
                //得到observerWraperr的类对象
                val supperClass: Class<*> = observerWraper.javaClass.superclass
                val mLastVersion = supperClass.getDeclaredField("mLastVersion")
                mLastVersion.isAccessible = true
                //2.得到mVersion
                val mVersion = liveDataClass.getDeclaredField("mVersion")
                mVersion.isAccessible = true
                //3.mLastVersion=mVersion
                val mVersionValue = mVersion[this]
                mLastVersion[observerWraper] = mVersionValue
            } catch (e: Exception) {
            }
        }
    }

    fun clean() {
        if (liveDataBus != null) {
            liveDataBus.bus.clear();
            liveDataBus.clean();
        }
    }
}