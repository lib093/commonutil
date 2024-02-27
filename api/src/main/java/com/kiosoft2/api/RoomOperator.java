package com.kiosoft2.api;

import androidx.room.RoomDatabase;

import com.kiosoft2.api.builder.OpBuilder;

import java.util.concurrent.Callable;

/**
 
 * @createDate: 2023/8/8 11:08
 * @version: 1.0
 * @description: Room 数据操作
 */
public abstract class RoomOperator extends RoomDatabase {

    /**
     * Room数据库操作者
     */
    private static RoomOperator db;

    /**
     * 是否输出sql日志到控制台
     */
    public static boolean PRINT_SQL_LOG;

    protected RoomOperator() {
        db = this;
    }

    /**
     * 获取Room数据库操作者
     */
    public static RoomOperator db() {
        return db;
    }

    /**
     * 获取entity 信息
     */
    public abstract <T> Entity<T> getEntity(Class<T> entityClass);

    /**
     * 数据表操作
     *
     * @param entityClass 实体class
     */
    public static <T> OpBuilder<T> op(Class<T> entityClass) {
        return new OpBuilder<>(db, db.getEntity(entityClass));
    }

    /**
     * 事务中执行
     *
     * @param runnable 执行体
     */
    public static void runInTx(Runnable runnable) {
        db.runInTransaction(runnable);
    }

    /**
     * 事务中执行
     *
     * @param callable 带有返回结果的执行体
     */
    public static <T> T runInTx(Callable<T> callable) {
        return db.runInTransaction(callable);
    }
}
