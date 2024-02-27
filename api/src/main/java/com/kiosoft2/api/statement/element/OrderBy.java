package com.kiosoft2.api.statement.element;

import com.kiosoft2.api.Property;
import com.kiosoft2.api.type.OrderByType;

public class OrderBy {

    /**排序字段*/
    public Property[] property;

    /**排序类型*/
    public OrderByType type;
}
