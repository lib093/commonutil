package com.kiosoft2.common.autoservice

import java.util.ServiceLoader

object ServiceLoad {
    fun <S> load(service: Class<S>?): S? {
        val iterator = ServiceLoader.load(service).iterator()
        return try {
            if (iterator.hasNext()) {
                iterator.next()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

}