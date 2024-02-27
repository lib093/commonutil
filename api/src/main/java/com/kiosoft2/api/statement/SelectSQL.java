package com.kiosoft2.api.statement;

import com.kiosoft2.api.Entity;
import com.kiosoft2.api.Property;
import com.kiosoft2.api.helper.SQLiteHelper;
import com.kiosoft2.api.statement.element.Condition;
import com.kiosoft2.api.statement.element.Expression;
import com.kiosoft2.api.statement.element.Function;
import com.kiosoft2.api.statement.element.GroupBy;
import com.kiosoft2.api.statement.element.Join;
import com.kiosoft2.api.statement.element.OrderBy;
import com.kiosoft2.api.statement.element.Page;
import com.kiosoft2.api.type.OpType;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 
 * @createDate: 2023/8/15 17:43
 * @version: 1.0
 * @description: select sql对象
 */
public class SelectSQL<T> extends SQL<T>{

    /**聚合函数对象*/
    public Function function;

    /**是否去重*/
    public boolean distinct;

    /**查询列*/
    private List<Property> columns;

    /**join列表*/
    public List<Join> joins;

    /**where条件*/
    public Condition where;

    /**分组对象*/
    public GroupBy groupBy;

    /**排序对象*/
    public OrderBy orderBy;

    /**分页信息*/
    public Page page;

    /**查询结果数据类型*/
    private Class<?> resultType;

    public SelectSQL(Entity<T> from) {
        super(OpType.select,from);
    }

    /**
     * 获取结果数据类型
     */
    public Type getResultType() {
        return resultType == null ? from.getEntityClass() : resultType;
    }

    /**
     * 设置结果数据类型
     */
    public void setResultType(Class<?> resultType) {
        this.resultType = resultType;
    }

    /**
     * 获取查询字段列
     */
    public List<Property> getColumns() {
        return isHasColumn() ? columns : from.getAllProperties();
    }

    /**
     * 设置查询字段列
     */
    public void setColumns(List<Property> columns) {
        this.columns = columns;
    }

    /**
     * 是否含有指定查询列
     */
    public boolean isHasColumn(){
        return columns != null && !columns.isEmpty();
    }

    /**
     * 是否属于指定获取单列
     */
    public boolean isSingleColumn(){
        if(function != null){
            return true;
        }

        if(resultType == null || columns == null || columns.isEmpty()){
            return false;
        }

        switch (resultType.getName()){
            case "java.lang.Boolean":
            case "boolean":
            case "java.lang.Integer":
            case "int":
            case "java.lang.Long":
            case "long":
            case "java.lang.Byte":
            case "byte":
            case "java.lang.Short":
            case "short":
            case "java.lang.Float":
            case "float":
            case "java.lang.Double":
            case "double":
            case "java.lang.String":
                return true;
            default:
                return false;
        }
    }

    @Override
    public int getArgumentCount() {
        int argumentCount = 0;
        if(joins != null && !joins.isEmpty()){
            for(Join item : joins){
                if(item.on == null){
                    continue;
                }
                argumentCount += item.on.getArgumentCount();
            }
        }
        if(where != null){
            argumentCount += where.getArgumentCount();
        }
        return argumentCount;
    }

    @Override
    public List<Expression> getArgumentList() {
        List<Expression> result = new ArrayList<>();
        if(joins != null && !joins.isEmpty()){
            for(Join item : joins){
                if(item.on == null){
                    continue;
                }
                result.addAll(item.on.getConditionList());
            }
        }
        if(where != null && where.isValid()){
            result.addAll(where.getConditionList());
        }
        return result;
    }

    @Override
    public String getSql() {
        return SQLiteHelper.generateSelectSql(this);
    }
}
