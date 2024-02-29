package com.kiosoft2.encrypt

import androidx.annotation.Keep
import com.google.auto.service.AutoService
import com.kiosoft2.common.autoservice.service.TaskService
import com.kiosoft2.common.encrypt.EncryptService
import java.util.Locale
@AutoService(EncryptService::class)
class EncryptServiceImpl: EncryptService {
    var tEstEntry = TEstEntry()
    override fun getEncrypt(str: String):String? {
        tEstEntry.getEncrypt222222(str)
        return str+"加密后的数据"
    }
}