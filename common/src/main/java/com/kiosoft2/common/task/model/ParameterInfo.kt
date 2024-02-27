package com.kiosoft2.common.task.model

import java.io.Serializable

class ParameterInfo:Serializable {
    var parameterType:Class<*>? = null
    var value:String? = null
}