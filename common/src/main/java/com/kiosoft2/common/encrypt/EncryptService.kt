package com.kiosoft2.common.encrypt

interface EncryptService {
    /**
     * 获取加密数据
     */
    fun getEncrypt(str:String):String?

}