package com.kiosoft2.api.statement.element;

import com.kiosoft2.api.Property;
import com.kiosoft2.api.type.FuncType;

/**
 
 * @createDate: 2023/8/17 14:52
 * @version: 1.0
 * @description: 聚合函数信息对象
 */
public class Function {

    /**聚合函数类型*/
    public final FuncType type;

    /**聚合字段*/
    public final Property property;

    public Function(FuncType type, Property property) {
        this.type = type;
        this.property = property;
    }
}
