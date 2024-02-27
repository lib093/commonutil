package com.kiosoft2.api.builder;

import androidx.annotation.NonNull;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.OnConflictStrategy;
import androidx.room.RoomDatabase;

import com.kiosoft2.api.Entity;
import com.kiosoft2.api.Property;
import com.kiosoft2.api.helper.SQLiteHelper;
import com.kiosoft2.api.statement.DeleteSQL;
import com.kiosoft2.api.statement.SelectSQL;
import com.kiosoft2.api.statement.element.Condition;
import com.kiosoft2.api.statement.element.Function;
import com.kiosoft2.api.statement.element.Page;
import com.kiosoft2.api.type.FuncType;
import com.kiosoft2.api.type.JoinType;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 
 * @createDate: 2023/8/8 11:45
 * @version: 1.0
 * @description: 数据表操作builder
 */
public class OpBuilder<T> {

    private final RoomDatabase db;
    private final Entity<T> entity;


    public OpBuilder(RoomDatabase db, Entity<T> entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity not found");
        }
        this.db = db;
        this.entity = entity;
    }

    //--------------------------------------------insert--------------------------------------------

    /**
     * 插入数据(若数据已存在会抛出异常)
     *
     * @param data 数据
     * @return 插入的数据主键id
     */
    public Long insert(final T data) {
        db.assertNotSuspendingTransaction();
        db.beginTransaction();
        try {
            long _result = SQLiteHelper.getEntityInsertionAdapter(db, entity, OnConflictStrategy.ABORT)
                    .insertAndReturnId(data);
            db.setTransactionSuccessful();
            return _result;
        } finally {
            db.endTransaction();
        }
    }

    /**
     * 插入数据(若数据已存在会抛出异常)
     *
     * @param data 数据
     * @return 插入的数据主键id
     */
    @SafeVarargs
    public final Long[] insert(final T... data) {
        db.assertNotSuspendingTransaction();
        db.beginTransaction();
        try {
            final Long[] _result = SQLiteHelper.getEntityInsertionAdapter(db, entity, OnConflictStrategy.ABORT)
                    .insertAndReturnIdsArrayBox(data);
            db.setTransactionSuccessful();
            return _result;
        } finally {
            db.endTransaction();
        }
    }

    /**
     * 插入数据(若数据已存在会抛出异常)
     *
     * @param data 数据
     * @return 插入的数据主键id
     */
    public List<Long> insert(final Collection<T> data) {
        db.assertNotSuspendingTransaction();
        db.beginTransaction();
        try {
            final List<Long> _result = SQLiteHelper.getEntityInsertionAdapter(db, entity, OnConflictStrategy.ABORT)
                    .insertAndReturnIdsList(data);
            db.setTransactionSuccessful();
            return _result;
        } finally {
            db.endTransaction();
        }
    }

    //----------------------------------------------save--------------------------------------------

    /**
     * 保存数据(若数据已存在会覆盖)
     *
     * @param data 数据
     * @return 插入的数据主键id
     */
    public Long save(final T data) {
        db.assertNotSuspendingTransaction();
        db.beginTransaction();
        try {
            final Long _result = SQLiteHelper.getEntityInsertionAdapter(db, entity, OnConflictStrategy.REPLACE)
                    .insertAndReturnId(data);
            db.setTransactionSuccessful();
            return _result;
        } finally {
            db.endTransaction();
        }
    }

    /**
     * 保存数据(若数据已存在会覆盖)
     *
     * @param data 数据
     * @return 插入的数据主键id
     */
    @SafeVarargs
    public final Long[] save(final T... data) {
        db.assertNotSuspendingTransaction();
        db.beginTransaction();
        try {
            final Long[] _result = SQLiteHelper.getEntityInsertionAdapter(db, entity, OnConflictStrategy.REPLACE)
                    .insertAndReturnIdsArrayBox(data);
            db.setTransactionSuccessful();
            return _result;
        } finally {
            db.endTransaction();
        }
    }

    /**
     * 保存数据(若数据已存在会覆盖)
     *
     * @param data 数据
     * @return 插入的数据主键id
     */
    public List<Long> save(final Collection<T> data) {
        db.assertNotSuspendingTransaction();
        db.beginTransaction();
        try {
            final List<Long> _result = SQLiteHelper.getEntityInsertionAdapter(db, entity, OnConflictStrategy.REPLACE)
                    .insertAndReturnIdsList(data);
            db.setTransactionSuccessful();
            return _result;
        } finally {
            db.endTransaction();
        }
    }

    //----------------------------------------delete------------------------------------------------

    /**
     * 根据主键id删除
     *
     * @param id 主键id
     * @return 影响行数
     */
    public int deleteById(final long id) {
        final DeleteSQL<T> deleteSQL = new DeleteSQL<>(entity);
        deleteSQL.where = Condition.equalId(entity.getIdProperty(), id);
        return SQLiteHelper.execDeleteSql(db, deleteSQL);
    }

    /**
     * 根据主键id删除
     *
     * @param id 主键id
     * @return 影响行数
     */
    public int deleteById(final String id) {
        final DeleteSQL<T> deleteSQL = new DeleteSQL<>(entity);
        deleteSQL.where = Condition.equalId(entity.getIdProperty(), id);
        return SQLiteHelper.execDeleteSql(db, deleteSQL);
    }

    /**
     * 根据批量主键id删除
     *
     * @param ids 主键id
     * @return 影响行数
     */
    public int deleteByIds(final Long... ids) {
        final DeleteSQL<T> deleteSQL = new DeleteSQL<>(entity);
        deleteSQL.where = Condition.inId(entity.getIdProperty(), ids);
        return SQLiteHelper.execDeleteSql(db, deleteSQL);
    }

    /**
     * 根据批量主键id删除
     *
     * @param ids 主键id
     * @return 影响行数
     */
    public int deleteByIds(final Integer... ids) {
        final DeleteSQL<T> deleteSQL = new DeleteSQL<>(entity);
        deleteSQL.where = Condition.inId(entity.getIdProperty(), ids);
        return SQLiteHelper.execDeleteSql(db, deleteSQL);
    }

    /**
     * 根据批量主键id删除
     *
     * @param ids 主键id
     * @return 影响行数
     */
    public int deleteByIds(final String... ids) {
        final DeleteSQL<T> deleteSQL = new DeleteSQL<>(entity);
        deleteSQL.where = Condition.inId(entity.getIdProperty(), ids);
        return SQLiteHelper.execDeleteSql(db, deleteSQL);
    }

    /**
     * 根据批量主键id删除
     *
     * @param ids 主键id
     * @return 影响行数
     */
    public int deleteByIds(final Collection<?> ids) {
        final DeleteSQL<T> deleteSQL = new DeleteSQL<>(entity);
        deleteSQL.where = Condition.inId(entity.getIdProperty(), ids.toArray());
        return SQLiteHelper.execDeleteSql(db, deleteSQL);
    }

    /**
     * 批量根据主键id删除
     *
     * @param data 删除对象
     * @return 影响行数
     */
    public int delete(final T data) {
        db.assertNotSuspendingTransaction();
        db.beginTransaction();
        try {
            final int result = SQLiteHelper.getEntityDeleteAdapter(db, entity).handle(data);
            db.setTransactionSuccessful();
            return result;
        } finally {
            db.endTransaction();
        }
    }

    /**
     * 批量根据主键id删除
     *
     * @param data 删除对象
     * @return 影响行数
     */
    @SafeVarargs
    public final int delete(final T... data) {
        db.assertNotSuspendingTransaction();
        db.beginTransaction();
        try {
            final int result = SQLiteHelper.getEntityDeleteAdapter(db, entity).handleMultiple(data);
            db.setTransactionSuccessful();
            return result;
        } finally {
            db.endTransaction();
        }
    }

    /**
     * 批量根据主键id删除
     *
     * @param data 删除对象
     * @return 影响行数
     */
    public int delete(final Collection<T> data) {
        db.assertNotSuspendingTransaction();
        db.beginTransaction();
        try {
            final int result = SQLiteHelper.getEntityDeleteAdapter(db, entity).handleMultiple(data);
            db.setTransactionSuccessful();
            return result;
        } finally {
            db.endTransaction();
        }
    }

    /**
     * 删除当前表全部数据
     *
     * @return 影响行数
     */
    public int deleteAll() {
        return SQLiteHelper.execDeleteSql(db, new DeleteSQL<>(entity));
    }

    /**
     * 去构建delete
     */
    public DeleteBuilder<T> deleteBuilder() {
        return new DeleteBuilder<>(db, entity);
    }

    //----------------------------------------update------------------------------------------------

    /**
     * 更新对象
     *
     * @param data 更新对象
     * @return 影响行数
     */
    public int update(final T data) {
        db.assertNotSuspendingTransaction();
        db.beginTransaction();
        try {
            final int _result = SQLiteHelper.getEntityUpdateAdapter(db, entity, OnConflictStrategy.ABORT)
                    .handle(data);
            db.setTransactionSuccessful();
            return _result;
        } finally {
            db.endTransaction();
        }
    }

    /**
     * 更新对象
     *
     * @param data 更新对象
     * @return 影响行数
     */
    @SafeVarargs
    public final int update(final T... data) {
        db.assertNotSuspendingTransaction();
        db.beginTransaction();
        try {
            final int _result = SQLiteHelper.getEntityUpdateAdapter(db, entity, OnConflictStrategy.ABORT)
                    .handleMultiple(data);
            db.setTransactionSuccessful();
            return _result;
        } finally {
            db.endTransaction();
        }
    }

    /**
     * 更新对象
     *
     * @param data 更新对象
     * @return 影响行数
     */
    public int update(final Collection<T> data) {
        db.assertNotSuspendingTransaction();
        db.beginTransaction();
        try {
            final int _result = SQLiteHelper.getEntityUpdateAdapter(db, entity, OnConflictStrategy.ABORT)
                    .handleMultiple(data);
            db.setTransactionSuccessful();
            return _result;
        } finally {
            db.endTransaction();
        }
    }

    /**
     * 去构造update
     */
    public SetBuilder<T> updateBuilder() {
        return new SetBuilder<>(db, entity);
    }

    //-----------------------------------------query------------------------------------------------

    /**
     * 根据主键id查找
     *
     * @param id 主键id
     * @return 结果
     */
    public T findById(final long id) {
        SelectSQL<T> selectSQL = new SelectSQL<>(entity);
        selectSQL.where = Condition.equalId(entity.getIdProperty(), id);
        return SQLiteHelper.execQuerySqlReturnOne(db, selectSQL);
    }

    /**
     * 根据主键id查找
     *
     * @param id 主键id
     * @return 结果
     */
    public T findById(final String id) {
        SelectSQL<T> selectSQL = new SelectSQL<>(entity);
        selectSQL.where = Condition.equalId(entity.getIdProperty(), id);
        return SQLiteHelper.execQuerySqlReturnOne(db, selectSQL);
    }

    /**
     * 根据批量主键id查找
     *
     * @param ids 主键id数组
     * @return 结果
     */
    public List<T> find(final Long... ids) {
        SelectSQL<T> selectSQL = new SelectSQL<>(entity);
        selectSQL.where = Condition.inId(entity.getIdProperty(), ids);
        return SQLiteHelper.execQuerySql(db, selectSQL);
    }

    /**
     * 根据批量主键id查找
     *
     * @param ids 主键id数组
     * @return 结果
     */
    public List<T> find(final Integer... ids) {
        SelectSQL<T> selectSQL = new SelectSQL<>(entity);
        selectSQL.where = Condition.inId(entity.getIdProperty(), ids);
        return SQLiteHelper.execQuerySql(db, selectSQL);
    }

    /**
     * 根据批量主键id查找
     *
     * @param ids 主键id数组
     * @return 结果
     */
    public List<T> find(final String... ids) {
        SelectSQL<T> selectSQL = new SelectSQL<>(entity);
        selectSQL.where = Condition.inId(entity.getIdProperty(), ids);
        return SQLiteHelper.execQuerySql(db, selectSQL);
    }

    /**
     * 根据批量主键id查找
     *
     * @param ids 主键id List
     * @return 结果
     */
    public List<T> find(final Collection<?> ids) {
        SelectSQL<T> selectSQL = new SelectSQL<>(entity);
        selectSQL.where = Condition.inId(entity.getIdProperty(), ids.toArray());
        return SQLiteHelper.execQuerySql(db, selectSQL);
    }

    /**
     * 分页查询记录
     *
     * @param limit  每页记录数量
     * @param offset 偏移量
     */
    public List<T> find(int limit, int offset) {
        SelectSQL<T> selectSQL = new SelectSQL<>(entity);
        selectSQL.page = new Page(limit, offset);
        return SQLiteHelper.execQuerySql(db, selectSQL);
    }

    /**
     * 分页查询记录
     *
     * @param pageSize 每页记录数量
     * @param pageNo   页码（从1开始）
     */
    public List<T> findPage(int pageSize, int pageNo) {
        SelectSQL<T> selectSQL = new SelectSQL<>(entity);
        selectSQL.page = new Page(pageNo, pageSize * (pageNo - 1));
        return SQLiteHelper.execQuerySql(db, selectSQL);
    }

    /**
     * 查询全部记录
     */
    public List<T> findAll() {
        return SQLiteHelper.execQuerySql(db, new SelectSQL<>(entity));
    }

    /**
     * 内连接
     *
     * @param target 目标表实体
     */
    public JoinBuilder<T> innerJoin(Entity<?> target) {
        return new JoinBuilder<>(JoinType.innerJoin, db, entity, target);
    }

    /**
     * 左连接
     *
     * @param target 目标表实体
     */
    public JoinBuilder<T> leftJoin(Entity<?> target) {
        return new JoinBuilder<>(JoinType.leftOuterJoin, db, entity, target);
    }

    /**
     * 交叉连接
     *
     * @param target 目标表实体
     */
    public JoinBuilder<T> crossJoin(Entity<?> target) {
        return new JoinBuilder<>(JoinType.crossJoin, db, entity, target);
    }

    /**
     * 查询builder
     */
    public QueryBuilder<T> query() {
        return new QueryBuilder<T>(db, entity, null, null);
    }

    /**
     * 查询builder
     *
     * @param columns 查询字段列表
     */
    public QueryBuilder<T> query(Property... columns) {
        return new QueryBuilder<>(db, entity, Arrays.asList(columns), null);
    }

    /**
     * 查询builder
     *
     * @param columns 查询字段列表
     */
    public QueryBuilder<T> query(List<Property> columns) {
        return new QueryBuilder<>(db, entity, columns, null);
    }

    public long count() {
        SelectSQL<T> selectSQL = new SelectSQL<>(entity);
        selectSQL.function = new Function(FuncType.count, null);
        selectSQL.setResultType(Long.class);

        Long result = SQLiteHelper.execQuerySqlReturnOne(db, selectSQL);
        return result != null ? result : 0;
    }

    public double max(@NonNull Property property) {
        SelectSQL<T> selectSQL = new SelectSQL<>(entity);
        selectSQL.function = new Function(FuncType.max, property);
        selectSQL.setResultType(double.class);

        Double result = SQLiteHelper.execQuerySqlReturnOne(db, selectSQL);
        return result != null ? result : 0;
    }

    public double min(@NonNull Property property) {
        SelectSQL<T> selectSQL = new SelectSQL<>(entity);
        selectSQL.function = new Function(FuncType.min, property);
        selectSQL.setResultType(double.class);

        Double result = SQLiteHelper.execQuerySqlReturnOne(db, selectSQL);
        return result != null ? result : 0;
    }

    public double avg(@NonNull Property property) {
        SelectSQL<T> selectSQL = new SelectSQL<>(entity);
        selectSQL.function = new Function(FuncType.avg, property);
        selectSQL.setResultType(double.class);

        Double result = SQLiteHelper.execQuerySqlReturnOne(db, selectSQL);
        return result != null ? result : 0;
    }

    public double sum(@NonNull Property property) {
        SelectSQL<T> selectSQL = new SelectSQL<>(entity);
        selectSQL.function = new Function(FuncType.sum, property);
        selectSQL.setResultType(double.class);

        Double result = SQLiteHelper.execQuerySqlReturnOne(db, selectSQL);
        return result != null ? result : 0;
    }
}
