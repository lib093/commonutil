package com.kiosoft2.api.builder;

import androidx.room.RoomDatabase;

import com.kiosoft2.api.helper.SQLiteHelper;
import com.kiosoft2.api.statement.UpdateSQL;

/**
 
 * @createDate: 2023/8/16 17:12
 * @version: 1.0
 * @description: update builder
 */
public class UpdateBuilder<T> extends BasicConditionRelationBuilder<T, UpdateBuilder<T>> {

    private final RoomDatabase db;
    private final UpdateSQL sql;

    public UpdateBuilder(RoomDatabase db, UpdateSQL updateSQL) {
        this.db = db;
        this.sql = updateSQL;
        this.sql.where = super.condition;
    }

    public int update() {
        return SQLiteHelper.execUpdateSql(db,sql);
    }
}
