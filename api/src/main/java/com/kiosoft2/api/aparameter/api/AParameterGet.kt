package com.kiosoft2.api.aparameter.api

interface AParameterGet {
    /**
     * 目标对象.属性名 = getIntent().属性类型... 完成赋值操作
     * @param targetParameter 目标对象：例如：MainActivity 中的那些属性
     */
    fun getParameter(targetParameter: Any)
}