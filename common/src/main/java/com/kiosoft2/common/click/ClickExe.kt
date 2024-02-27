package com.kiosoft2.common.click

import android.util.Log
import android.view.View
import androidx.annotation.Keep
import com.kiosoft2.common.click.utils.ClickUtil

@Keep
object ClickExe{
    @Keep
    @JvmStatic
    fun View.setSafeOnClickListener(
        onClick: (View) -> Unit
    ) {
        setOnClickListener {
            if (!ClickUtil.isFastDoubleClick()) {
                onClick(it)
            }else{
                Log.d("lance", "setSafeOnClickListener: 没执行")
            }
        }
    }
    @Keep
    @JvmStatic
    fun View.setSafeOnClickListener(
        listener: View.OnClickListener
    ) {
        setOnClickListener {
            if (!ClickUtil.isFastDoubleClick()) {
                listener.onClick(it)
                Log.d("lance", "setSafeOnClickListener: 执行了了了了了了了了")
            }else{
                Log.d("lance", "setSafeOnClickListener: 没执行")
            }
        }
    }
}
