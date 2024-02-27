package com.kiosoft2.api.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

/**
 
 * @createDate: 2023/04/10 21:14
 * @version: 1.0
 * @description: dao 基类
 */
public abstract class Dao<T> {

    @Insert
    public abstract Long insert(T data);

    @Insert
    public abstract Long[] insert(T[] data);

    @Insert
    public abstract List<Long> insert(List<T> data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Long save(T data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Long[] save(T[] data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract List<Long> save(List<T> data);

    @Delete
    public abstract int delete(T data);

    @Delete
    public abstract int delete(T[] data);

    @Delete
    public abstract int delete(List<T> data);

    @Update
    public abstract int update(T data);

    @Update
    public abstract int update(T[] data);

    @Update
    public abstract int update(List<T> data);

    /**
     * 执行sql，返回List
     *
     * @param query sql
     * @return List<T>
     */
    @RawQuery
    public abstract List<T> resultList(SupportSQLiteQuery query);

    /**
     * 执行sql，返回一条数据T
     *
     * @param query sql
     * @return T
     */
    @RawQuery
    public abstract T resultOne(SupportSQLiteQuery query);

    /**
     * 执行sql，返回影响行数
     *
     * @param query sql
     * @return int 影响行数
     */
    @RawQuery
    public abstract int resultRowNum(SupportSQLiteQuery query);
}
