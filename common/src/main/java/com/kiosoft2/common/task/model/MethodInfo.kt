package com.kiosoft2.common.task.model

import java.io.Serializable

class MethodInfo:Serializable {
    var owerClassName:Class<*>? = null
    var methouName:String = ""
    var args:MutableList<ParameterInfo> = mutableListOf()
}