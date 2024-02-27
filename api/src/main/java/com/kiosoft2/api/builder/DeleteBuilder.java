package com.kiosoft2.api.builder;

import androidx.room.RoomDatabase;

import com.kiosoft2.api.Entity;
import com.kiosoft2.api.helper.SQLiteHelper;
import com.kiosoft2.api.statement.DeleteSQL;

/**
 
 * @createDate: 2023/8/16 17:12
 * @version: 1.0
 * @description: delete builder
 */
public class DeleteBuilder<T> extends BasicConditionRelationBuilder<T, DeleteBuilder<T>> {

    private final RoomDatabase db;
    private final DeleteSQL<T> sql;

    public DeleteBuilder(RoomDatabase db, Entity<T> entity) {
        this.db = db;
        this.sql = new DeleteSQL<>(entity);
        this.sql.where = super.condition;
    }

    public int delete() {
        return SQLiteHelper.execDeleteSql(db,sql);
    }
}
