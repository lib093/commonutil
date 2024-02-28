package com.kiosoft2.encrypt

import com.google.auto.service.AutoService
import com.kiosoft2.common.autoservice.service.TaskService
import com.kiosoft2.common.encrypt.EncryptService
import java.util.Locale
@AutoService(EncryptService::class)
class EncryptServiceImpl: EncryptService {
    override fun getEncrypt(str: String):String? {
        var hex = str
        if(hex == null || hex.length == 0) {
            return null
        }
        if (hex.length % 2 != 0) {
            hex = "0$hex"
        }
        val hexString: String = hex.uppercase(Locale.getDefault())
        val len = hexString.length
        val data = ByteArray(len / 2)
        var i = 0
        return str+"加密后的数据"
    }
}