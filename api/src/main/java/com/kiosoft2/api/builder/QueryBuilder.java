package com.kiosoft2.api.builder;

import androidx.room.RoomDatabase;

import com.kiosoft2.api.Entity;
import com.kiosoft2.api.Property;
import com.kiosoft2.api.statement.SelectSQL;
import com.kiosoft2.api.statement.element.Join;

import java.util.List;

/**
 
 * @createDate: 2023/8/8 16:10
 * @version: 1.0
 * @description: select查询builder
 */
public class QueryBuilder<T> extends BasicConditionRelationBuilder<T, QueryBuilder<T>> {

    private final RoomDatabase db;
    private final SelectSQL<T> sql;

    public QueryBuilder(RoomDatabase db, Entity<T> entity, List<Property> columns, List<Join> joins) {
        this.db = db;
        this.sql = new SelectSQL<>(entity);
        this.sql.setColumns(columns);
        this.sql.joins = joins;
        this.sql.where = super.condition;
    }

    public Query<T> build() {
        return new Query<>(db, sql);
    }

    public ChildQuery buildChildQuery() {
        return new ChildQuery(sql);
    }
}
