package com.kiosoft2.api;

import androidx.annotation.Keep;
import androidx.sqlite.db.SupportSQLiteStatement;

import java.io.Serializable;
import java.util.List;

/**

 * @version: 1.0
 * @description: 实体信息
 */
@Keep
public interface Entity<T> extends Serializable {

    /**
     * 获取表名
     */
    String getTableName();

    /**
     * 获取主键id 属性
     */
    Property getIdProperty();

    /**
     * 获取复合主键
     */
    List<Property> getCompositePK();

    /**
     * 获取当前表中全部字段
     */
    List<Property> getAllProperties();

    /**
     * 获取实体class
     */
    Class<?> getEntityClass();

    /**
     * 提供绑定该实体对象insert操作的属性值
     * @param stmt sqlite语句句柄
     * @param value 绑定对象
     */
    void bindInsertionAdapterValue(SupportSQLiteStatement stmt, T value);

    /**
     * 提供绑定该实体对象根据主键删除操作的属性值
     * @param stmt sqlite语句句柄
     * @param value 绑定对象
     */
    void bindDeleteAdapterValue(SupportSQLiteStatement stmt, T value);


    /**
     * 提供绑定该实体对象根据主键更新操作的属性值
     * @param stmt sqlite语句句柄
     * @param value 绑定对象
     */
    void bindUpdateAdapterValue(SupportSQLiteStatement stmt, T value);
}
