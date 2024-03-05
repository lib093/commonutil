package com.kiosoft2.common.router

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import java.io.Serializable
import java.util.ArrayList

class ARouterOperator private constructor() {
    companion object {
       private val instance: ARouterOperator by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ARouterOperator() }
        fun<T : Activity> op(page: Class<T>):ActivityBundleManager{
            return instance.ActivityBundleManager(page)
        }
        fun<T : Fragment> op(page: Class<T>):FragmentBundleManager{
            return instance.FragmentBundleManager(page)
        }
    }
    private fun navigationFragment(bundleManager: FragmentBundleManager):Fragment?{
        var fragment = bundleManager.page.newInstance() as Fragment
        fragment.arguments = bundleManager.bundle
        return fragment
    }


    private fun navigationActivity(aty: Activity,bundleManager: BundleManager, code:Int?,isFinish:Boolean){
            val intent = Intent(
                aty,
                bundleManager.page
            )
            intent.putExtras(bundleManager.bundle) // 携带参数
            if (null != code)
                aty.startActivityForResult(intent, code)
            else
                aty.startActivity(intent)
        if (isFinish)
            aty.finish()
    }


    inner class ActivityBundleManager( page: Class<*>):BundleManager(page) {
        override fun withString(key: String, value: String): ActivityBundleManager {
            return super.withString(key, value) as ActivityBundleManager
        }

        override fun withBoolean(key: String, value: Boolean): ActivityBundleManager {
            return super.withBoolean(key, value) as ActivityBundleManager
        }

        override fun withBundle(key: String, value: Bundle): ActivityBundleManager {
            return super.withBundle(key, value)as ActivityBundleManager
        }

        override fun withInt(key: String, value: Int): ActivityBundleManager {
            return super.withInt(key, value) as ActivityBundleManager
        }

        override fun withParcelableArrayList(
            key: String,
            values: ArrayList<out Parcelable>?
        ): ActivityBundleManager {
            return super.withParcelableArrayList(key, values)as ActivityBundleManager
        }

        override fun withSerializable(key: String, value: Serializable): ActivityBundleManager {
            return super.withSerializable(key, value)as ActivityBundleManager
        }
        @SuppressLint("NewApi")
        fun navigation(aty:Activity){
            return navigation(aty,null)
        }
        fun navigation(aty:Activity,isFinish:Boolean){
            return navigation(aty,null,isFinish)
        }
        @SuppressLint("NewApi")
        fun navigation(aty:Activity,code:Int?){
            return navigationActivity(aty,this,code,false)
        }
        fun navigation(aty:Activity,code:Int?,isFinish:Boolean){
            return this@ARouterOperator.navigationActivity(aty,this,code,isFinish)
        }
        @SuppressLint("NewApi")
        fun navigation(fragment: Fragment):Any?{
            return  navigation(fragment,null)
        }
        fun navigation(fragment: Fragment,isFinish:Boolean):Any?{
            return  navigation(fragment,null,isFinish)
        }
        @SuppressLint("NewApi")
        fun navigation(fragment: Fragment,code:Int?){
            return navigationActivity(fragment.requireActivity(),this,code,false)
        }
        @SuppressLint("NewApi")
        fun navigation(fragment: Fragment,code:Int?,isFinish:Boolean){
            return this@ARouterOperator.navigationActivity(fragment.requireActivity(),this,code,isFinish)
        }
    }

    inner class FragmentBundleManager(page: Class<*>):BundleManager(page) {
        override fun withString(key: String, value: String): FragmentBundleManager {
            return super.withString(key, value) as FragmentBundleManager
        }

        override fun withBoolean(key: String, value: Boolean): FragmentBundleManager {
            return super.withBoolean(key, value) as FragmentBundleManager
        }

        override fun withBundle(key: String, value: Bundle): FragmentBundleManager {
            return super.withBundle(key, value)as FragmentBundleManager
        }

        override fun withInt(key: String, value: Int): FragmentBundleManager {
            return super.withInt(key, value) as FragmentBundleManager
        }

        override fun withParcelableArrayList(
            key: String,
            values: ArrayList<out Parcelable>?
        ): FragmentBundleManager {
            return super.withParcelableArrayList(key, values)as FragmentBundleManager
        }

        override fun withSerializable(key: String, value: Serializable): FragmentBundleManager {
            return super.withSerializable(key, value)as FragmentBundleManager
        }
        @SuppressLint("NewApi")
        fun navigation():Fragment?{
            return this@ARouterOperator.navigationFragment(this)
        }

    }
}