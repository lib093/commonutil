package com.kiosoft2.testdemo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.kiosoft2.annotation.AParameter
import com.kiosoft2.api.aparameter.AParameterManager

class Test1Fragment:Fragment() {
    @AParameter
    var name:String? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AParameterManager.instance.load(this)
    }
}