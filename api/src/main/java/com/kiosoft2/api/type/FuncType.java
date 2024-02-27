package com.kiosoft2.api.type;

public enum FuncType {

    count,//计算一个数据库表中的行数
    max,//获取某列最大值
    min,//获取某列最小值
    avg,//获取某列平均值
    sum,//获取某列总和值
    abs,//返回参数的绝对值
    upper,//把字符串转换为大写字母
    lower,//把字符串转换为小写字母
    length,//返回字符串的长度
    strFTime,//格式化时间戳或时间串(自定义格式)
    dateTime,//时间戳转换时间为yyyy-MM-dd HH:mm:ss格式
    date,//时间戳转换时间为yyyy-MM-dd格式
    time,//时间戳转换时间为HH:mm:ss格式
    ;

}
