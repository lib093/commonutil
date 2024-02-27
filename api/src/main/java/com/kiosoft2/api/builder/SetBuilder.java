package com.kiosoft2.api.builder;

import androidx.room.RoomDatabase;

import com.kiosoft2.api.Entity;
import com.kiosoft2.api.Property;
import com.kiosoft2.api.statement.UpdateSQL;
import com.kiosoft2.api.statement.element.Expression;
import com.kiosoft2.api.type.ExpressionType;

import java.util.Date;

/**
 
 * @createDate: 2023/8/17 9:22
 * @version: 1.0
 * @description: update set builder
 */
public class SetBuilder<T> {

    private final RoomDatabase db;
    private final UpdateSQL<T> sql;

    public SetBuilder(RoomDatabase db, Entity<T> entity) {
        this.db = db;
        this.sql = new UpdateSQL<>(entity);
    }

    public UpdateBuilder<T> build(){
        return new UpdateBuilder<>(db,sql);
    }

    //----------------------------------------------------------------------------------------------

    public SetBuilder<T> set(Property property, long value) {
        this.sql.setFields.add(new Expression(property, ExpressionType.equal,new Object[]{value}));
        return this;
    }

    public SetBuilder<T> set(Property property, double value) {
        this.sql.setFields.add(new Expression(property,ExpressionType.equal,new Object[]{value}));
        return this;
    }

    public SetBuilder<T> set(Property property, byte[] value) {
        this.sql.setFields.add(new Expression(property,ExpressionType.equal,new Object[]{value}));
        return this;
    }

    public SetBuilder<T> set(Property property, String value) {
        this.sql.setFields.add(new Expression(property,ExpressionType.equal,new Object[]{value}));
        return this;
    }

    public SetBuilder<T> set(Property property, boolean value) {
        this.sql.setFields.add(new Expression(property,ExpressionType.equal,new Object[]{value}));
        return this;
    }

    public SetBuilder<T> set(Property property, Date value) {
        this.sql.setFields.add(new Expression(property,ExpressionType.equal,new Object[]{value.getTime()}));
        return this;
    }

}
