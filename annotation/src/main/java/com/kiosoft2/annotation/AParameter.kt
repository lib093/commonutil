package com.kiosoft2.annotation

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.BINARY)
annotation class AParameter( // 不填写name的注解值表示该属性名就是key，填写了就用注解值作为key
 val name:String =""
)