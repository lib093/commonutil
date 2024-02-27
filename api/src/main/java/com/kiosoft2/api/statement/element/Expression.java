package com.kiosoft2.api.statement.element;

import com.kiosoft2.api.Property;
import com.kiosoft2.api.statement.SelectSQL;
import com.kiosoft2.api.type.ExpressionType;

/**
 
 * @createDate: 2023/8/8 16:21
 * @version: 1.0
 * @description: 表达式对象
 */
public class Expression {

    //-----------------------leftChildSelectSQL 和 property 二选一-------------------
    /**表达式左子查询SQL*/
    public final SelectSQL<?> leftChildSelectSQL;

    /**属性信息*/
    public final Property property;


    //-----------------------rightChildSelectSQL 和 value 二选一-------------------
    /**表达式右子查询SQL*/
    public final SelectSQL<?> rightChildSelectSQL;

    /**属性值*/
    public final Object[] value;

    //----------------------------------------------------------------------------------------------

    /**表达式类型*/
    public final ExpressionType expressionType;

    /**value字段是否属于Property字段属性信息（update set列时无效）*/
    public final boolean isValueProperty;


    public static Expression newValueProperty(Property property, ExpressionType expressionType, Object[] value){
        return new Expression(property, expressionType,value,true);
    }

    private Expression(Property property, ExpressionType expressionType, Object[] value, boolean isValueProperty) {
        this.leftChildSelectSQL = null;
        this.property = property;
        this.rightChildSelectSQL = null;
        this.value = value;
        this.expressionType = expressionType;
        this.isValueProperty = isValueProperty;
    }

    public Expression(Property property, ExpressionType expressionType) {
        this.leftChildSelectSQL = null;
        this.property = property;
        this.rightChildSelectSQL = null;
        this.value = null;
        this.expressionType = expressionType;
        this.isValueProperty = false;
    }

    public Expression(Property property, ExpressionType expressionType, Object[] value) {
        this.leftChildSelectSQL = null;
        this.property = property;
        this.rightChildSelectSQL = null;
        this.value = value;
        this.expressionType = expressionType;
        this.isValueProperty = false;
    }

    public Expression(ExpressionType expressionType, SelectSQL<?> rightChildSelectSQL) {
        this.leftChildSelectSQL = null;
        this.property = null;
        this.rightChildSelectSQL = rightChildSelectSQL;
        this.value = null;
        this.expressionType = expressionType;
        this.isValueProperty = false;
    }

    public Expression(Property property, ExpressionType expressionType, SelectSQL<?> rightChildSelectSQL) {
        this.leftChildSelectSQL = null;
        this.property = property;
        this.rightChildSelectSQL = rightChildSelectSQL;
        this.value = null;
        this.expressionType = expressionType;
        this.isValueProperty = false;
    }

    public Expression(SelectSQL<?> leftChildSelectSQL, ExpressionType expressionType) {
        this.leftChildSelectSQL = leftChildSelectSQL;
        this.property = null;
        this.rightChildSelectSQL = null;
        this.value = null;
        this.expressionType = expressionType;
        this.isValueProperty = false;
    }

    public Expression(SelectSQL<?> leftChildSelectSQL, ExpressionType expressionType, Object[] value) {
        this.leftChildSelectSQL = leftChildSelectSQL;
        this.property = null;
        this.rightChildSelectSQL = null;
        this.value = value;
        this.expressionType = expressionType;
        this.isValueProperty = false;
    }

    public Expression(SelectSQL<?> leftChildSelectSQL, ExpressionType expressionType, SelectSQL<?> rightChildSelectSQL) {
        this.leftChildSelectSQL = leftChildSelectSQL;
        this.property = null;
        this.rightChildSelectSQL = rightChildSelectSQL;
        this.value = null;
        this.expressionType = expressionType;
        this.isValueProperty = false;
    }
}
