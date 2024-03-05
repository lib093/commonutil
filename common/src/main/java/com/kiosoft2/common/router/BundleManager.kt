package com.kiosoft2.common.router

import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable
import java.util.ArrayList

open class BundleManager (var page: Class<*>) {
    val bundle = Bundle()
   open fun withString(key:String,value:String): BundleManager {
        bundle.putString(key,value)
        return this;
    }

    open fun withInt(key:String,value:Int): BundleManager {
        bundle.putInt(key,value)
        return this;
    }
    open fun withBoolean(key:String,value:Boolean): BundleManager {
        bundle.putBoolean(key,value)
        return this;
    }
    open fun withBundle(key:String,value: Bundle): BundleManager {
        bundle.putBundle(key,value)
        return this;
    }
    open fun withSerializable(key:String,value: Serializable): BundleManager {
        bundle.putSerializable(key,value)
        return this;
    }
    open fun withParcelableArrayList(key:String, values: ArrayList<out Parcelable>?): BundleManager {
        bundle.putParcelableArrayList(key, values)
        return this;
    }
}