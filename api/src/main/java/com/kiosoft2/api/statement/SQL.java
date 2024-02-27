package com.kiosoft2.api.statement;

import androidx.annotation.NonNull;

import com.kiosoft2.api.Entity;
import com.kiosoft2.api.statement.element.Expression;
import com.kiosoft2.api.type.OpType;

import java.util.List;

/**
 
 * @createDate: 2023/8/15 17:35
 * @version: 1.0
 * @description: sql抽象对象
 */
public abstract class SQL<T>{

    /**sql类型*/
    public final OpType opType;

    /**查询的表的实体信息*/
    public final Entity<T> from;

    public SQL(OpType opType,Entity<T> from){
        this.opType = opType;
        this.from = from;
    }

    @NonNull
    @Override
    public String toString() {
        return this.getSql();
    }

    /**
     * 获取sql参数数量
     */
    public abstract int getArgumentCount();

    /***
     * 获取绑定条件列表
     */
    public abstract List<Expression> getArgumentList();

    /**
     * 获取sql
     */
    public abstract String getSql();
}
