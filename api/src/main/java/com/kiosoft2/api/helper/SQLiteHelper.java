package com.kiosoft2.api.helper;

import android.database.Cursor;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.OnConflictStrategy;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteProgram;
import androidx.sqlite.db.SupportSQLiteStatement;

import com.alibaba.fastjson2.JSON;
import com.kiosoft2.api.Entity;
import com.kiosoft2.api.Property;
import com.kiosoft2.api.RoomOperator;
import com.kiosoft2.api.statement.DeleteSQL;
import com.kiosoft2.api.statement.SelectSQL;
import com.kiosoft2.api.statement.UpdateSQL;
import com.kiosoft2.api.statement.element.Condition;
import com.kiosoft2.api.statement.element.Expression;
import com.kiosoft2.api.statement.element.Join;
import com.kiosoft2.api.type.FuncType;
import com.kiosoft2.api.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 
 * @createDate: 2023/8/10 17:13
 * @version: 1.0
 * @description: sqlite 数据表 CURD
 */
@SuppressWarnings("unchecked")
public final class SQLiteHelper {

    private static final String TAG = "SQLiteHelper";

    private static final ArrayMap<String, EntityInsertionAdapter<?>> insertionAdapterMap = new ArrayMap<>();
    private static final ArrayMap<String, EntityDeletionOrUpdateAdapter<?>> deleteAdapterMap = new ArrayMap<>();
    private static final ArrayMap<String, EntityDeletionOrUpdateAdapter<?>> updateAdapterMap = new ArrayMap<>();

    private SQLiteHelper() {
    }


    /**
     * 获取EntityInsertionAdapter
     *
     * @param db                 RoomDatabase
     * @param entity             实体信息
     * @param onConflictStrategy insert 策略(详见OnConflictStrategy)
     */
    public static <T> EntityInsertionAdapter<T> getEntityInsertionAdapter(RoomDatabase db, Entity<T> entity, int onConflictStrategy) {
        String key = String.format("%s_%s", entity.getEntityClass().getName(), onConflictStrategy);

        EntityInsertionAdapter<?> value = insertionAdapterMap.get(key);
        if (value == null) {
            synchronized (SQLiteHelper.class) {
                value = SQLiteHelper.newEntityInsertionAdapter(db, entity, onConflictStrategy);
                insertionAdapterMap.put(key, value);
            }
        }
        return (EntityInsertionAdapter<T>) value;
    }

    /**
     * 获取delete adapter
     *
     * @param db     RoomDatabase
     * @param entity 实体信息
     */
    public static <T> EntityDeletionOrUpdateAdapter<T> getEntityDeleteAdapter(RoomDatabase db, Entity<T> entity) {
        EntityDeletionOrUpdateAdapter<?> value = deleteAdapterMap.get(entity.getEntityClass().getName());
        if (value == null) {
            synchronized (SQLiteHelper.class) {
                value = SQLiteHelper.newEntityDeleteAdapter(db, entity);
                deleteAdapterMap.put(entity.getEntityClass().getName(), value);
            }
        }
        return (EntityDeletionOrUpdateAdapter<T>) value;
    }

    /**
     * 获取update adapter
     *
     * @param db                 RoomDatabase
     * @param entity             实体信息
     * @param onConflictStrategy insert 策略(详见OnConflictStrategy)
     */
    public static <T> EntityDeletionOrUpdateAdapter<T> getEntityUpdateAdapter(RoomDatabase db, Entity<T> entity, int onConflictStrategy) {
        String key = String.format("%s_%s", entity.getEntityClass().getName(), onConflictStrategy);

        EntityDeletionOrUpdateAdapter<?> value = updateAdapterMap.get(key);
        if (value == null) {
            synchronized (SQLiteHelper.class) {
                value = SQLiteHelper.newEntityUpdateAdapter(db, entity, onConflictStrategy);
                updateAdapterMap.put(key, value);
            }
        }
        return (EntityDeletionOrUpdateAdapter<T>) value;
    }

    /**
     * 生成根据主键查询的sql语句
     *
     * @param entity    实体信息
     * @param idInCount 根据主键id in 查询参数数量（仅单主键id有效，复合主键无效）
     * @return sql语句
     */
    public static <T> String generatePKSelectSql(Entity<T> entity, int idInCount) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("select * from ")
                .append(entity.getTableName()).append(" ")
                .append("where ");

        SQLiteHelper.generatePKWhere(entity, idInCount, sqlBuilder);

        return sqlBuilder.toString();
    }

    /**
     * 生成select sql
     *
     * @param selectSQL 查询语句对象
     * @return sql
     */
    public static <T> String generateSelectSql(SelectSQL<T> selectSQL) {
        StringBuilder sqlBuilder = new StringBuilder();

        //opType
        sqlBuilder.append(selectSQL.opType.name()).append(" ");

        //function
        if (selectSQL.function != null) {
            sqlBuilder.append(selectSQL.function.type.name()).append("(");
            if (selectSQL.function.type == FuncType.count) {
                sqlBuilder.append("*");
            } else {
                sqlBuilder.append(selectSQL.function.property.name);
            }
            sqlBuilder.append(") ");
        } else {
            //columns
            if (selectSQL.distinct) {
                sqlBuilder.append("distinct ");
            }

            if (!selectSQL.isHasColumn()) {
                sqlBuilder.append("* ");
            } else {
                List<Property> columns = selectSQL.getColumns();
                Property property;
                for (int i = 0, size = columns.size(); i < size; i++) {
                    property = columns.get(i);

                    if (property.getFuncType() != null) {
                        sqlBuilder.append(property.getFuncType().name())
                                .append("(");

                        switch (property.getFuncType()) {
                            case strFTime:
                                sqlBuilder.append(String.format(
                                        "'%s', datetime(%s.%s/1000, 'unixepoch','localtime')"
                                        , property.getTimeFormat()
                                        , property.tableName
                                        , property
                                ));
                                break;
                            case dateTime:
                            case date:
                            case time:
                                sqlBuilder.append(String.format("%s.%s/1000, 'unixepoch','localtime'", property.tableName, property));
                                break;
                            default:
                                sqlBuilder
                                        .append(property.tableName)
                                        .append(".")
                                        .append(property);
                        }
                    } else {
                        sqlBuilder
                                .append(property.tableName)
                                .append(".")
                                .append(property);
                    }

                    if (property.getFuncType() != null) {
                        sqlBuilder.append(")");
                    }

                    sqlBuilder.append(" ");

                    if (!TextUtils.isEmpty(property.getAlias())) {
                        sqlBuilder.append(property.getAlias()).append(" ");
                    } else if (property.getFuncType() != null) {
                        sqlBuilder.append(property).append(" ");
                    }

                    if (i + 1 < size) {
                        sqlBuilder.append(", ");
                    }
                }
            }
        }

        //from
        sqlBuilder.append("from ").append(selectSQL.from.getTableName()).append(" ");

        //join
        if (selectSQL.joins != null && !selectSQL.joins.isEmpty()) {
            for (Join join : selectSQL.joins) {
                sqlBuilder.append(join.type.getExp()).append(" ")
                        .append(join.targetEntity.getTableName()).append(" ");

                if (join.on != null) {
                    sqlBuilder.append("on ");
                    SQLiteHelper.generateWhereSql(join.on, sqlBuilder);
                }
            }
        }

        //where
        if (selectSQL.where != null && selectSQL.where.isValid()) {
            sqlBuilder.append("where ");
            SQLiteHelper.generateWhereSql(selectSQL.where, sqlBuilder);
        }

        //groupBy
        if (selectSQL.groupBy != null) {
            sqlBuilder.append("group by ");
            for (int i = 0, length = selectSQL.groupBy.property.length; i < length; i++) {
                Property p = selectSQL.groupBy.property[i];
                sqlBuilder.append(p.tableName).append(".").append(p.name);
                if (i + 1 < length) {
                    sqlBuilder.append(",");
                }
            }
            sqlBuilder.append(" ");
        }

        //orderBy
        if (selectSQL.orderBy != null) {
            sqlBuilder.append("order by ");
            for (int i = 0, length = selectSQL.orderBy.property.length; i < length; i++) {
                Property p = selectSQL.orderBy.property[i];
                sqlBuilder.append(p.tableName).append(".").append(p.name);
                if (i + 1 < length) {
                    sqlBuilder.append(",");
                }
            }

            if (selectSQL.orderBy.type != null) {
                sqlBuilder.append(" ").append(selectSQL.orderBy.type.name());
            }
        }

        //limit
        if (selectSQL.page != null) {
            sqlBuilder.append(" limit ").append(selectSQL.page.limit)
                    .append(" offset ").append(selectSQL.page.offset)
                    .append(" ");
        }

        return sqlBuilder.toString();
    }

    /**
     * 生成根据主键更新的sql语句
     *
     * @param entity 实体信息
     * @return sql语句
     */
    public static <T> String generatePKUpdateSql(Entity<T> entity, int onConflictStrategy) {
        String strategy;
        switch (onConflictStrategy) {
            case OnConflictStrategy.REPLACE:
                strategy = "or replace ";
                break;
            case OnConflictStrategy.ROLLBACK:
                strategy = "or rollback ";
                break;
            case OnConflictStrategy.ABORT:
                strategy = "or abort ";
                break;
            case OnConflictStrategy.FAIL:
                strategy = "or fail ";
                break;
            case OnConflictStrategy.IGNORE:
                strategy = "or ignore ";
                break;
            case OnConflictStrategy.NONE:
            default:
                strategy = "";
        }

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("update ")
                .append(strategy)
                .append(entity.getTableName()).append(" set ");

        for (int i = 0, size = entity.getAllProperties().size(); i < size; i++) {
            sqlBuilder.append(entity.getAllProperties().get(i)).append(" = ? ");
            if (i + 1 < size) {
                sqlBuilder.append(", ");
            }
        }

        sqlBuilder.append("where ");
        SQLiteHelper.generatePKWhere(entity, 1, sqlBuilder);

        return sqlBuilder.toString();
    }

    /**
     * 生成Update sql
     *
     * @param updateSQL update sql对象
     * @return sql
     */
    public static <T> String generateUpdateSql(UpdateSQL<T> updateSQL) {
        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append(updateSQL.opType.name()).append(" ")
                .append(updateSQL.from.getTableName())
                .append(" set ");

        for (int i = 0, size = updateSQL.setFields.size(); i < size; i++) {
            sqlBuilder.append(updateSQL.setFields.get(i).property).append(" = ? ");
            if (i + 1 < size) {
                sqlBuilder.append(", ");
            }
        }

        if (updateSQL.where != null && updateSQL.where.isValid()) {
            sqlBuilder.append("where ");
            SQLiteHelper.generateWhereSql(updateSQL.where, sqlBuilder);
        }

        return sqlBuilder.toString();
    }

    /**
     * 生成根据主键删除的sql语句
     *
     * @param entity    实体信息
     * @param idInCount 根据主键id in 查询参数数量（仅单主键id有效，复合主键无效）
     * @return sql语句
     */
    public static <T> String generatePKDeleteSql(Entity<T> entity, int idInCount) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("delete from ")
                .append(entity.getTableName())
                .append(" where ");

        SQLiteHelper.generatePKWhere(entity, idInCount, sqlBuilder);

        return sqlBuilder.toString();
    }

    /**
     * 生成Delete sql
     *
     * @param deleteSQL delete sql对象
     * @return sql
     */
    public static <T> String generateDeleteSql(DeleteSQL<T> deleteSQL) {
        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append(deleteSQL.opType.name()).append(" from ")
                .append(deleteSQL.from.getTableName()).append(" ");

        if (deleteSQL.where != null && deleteSQL.where.isValid()) {
            sqlBuilder.append("where ");
            SQLiteHelper.generateWhereSql(deleteSQL.where, sqlBuilder);
        }

        return sqlBuilder.toString();
    }

    /**
     * 生成Insert sql
     *
     * @param entity             实体信息
     * @param onConflictStrategy insert语句时有效
     * @return sql
     */
    public static <T> String generateInsertSql(Entity<T> entity, int onConflictStrategy) {
        String strategy;
        switch (onConflictStrategy) {
            case OnConflictStrategy.REPLACE:
                strategy = "or replace ";
                break;
            case OnConflictStrategy.ROLLBACK:
                strategy = "or rollback ";
                break;
            case OnConflictStrategy.ABORT:
                strategy = "or abort ";
                break;
            case OnConflictStrategy.FAIL:
                strategy = "or fail ";
                break;
            case OnConflictStrategy.IGNORE:
                strategy = "or ignore ";
                break;
            case OnConflictStrategy.NONE:
            default:
                strategy = "";
        }

        StringBuilder sqlBuilder = new StringBuilder();
        StringBuilder valuesBuilder = new StringBuilder();

        sqlBuilder.append("insert ")
                .append(strategy)
                .append("into `")
                .append(entity.getTableName()).append("` ")
                .append("(");

        Property p;
        for (int i = 0, size = entity.getAllProperties().size(); i < size; i++) {
            p = entity.getAllProperties().get(i);

            sqlBuilder.append("`").append(p.name).append("`");

            if (p.isId) {
                valuesBuilder.append("nullif(?,0)");
            } else {
                valuesBuilder.append("?");
            }

            if (i + 1 < size) {
                sqlBuilder.append(",");
                valuesBuilder.append(",");
            }
        }

        sqlBuilder.append(") ")
                .append("values (")
                .append(valuesBuilder)
                .append(")");

        return sqlBuilder.toString();
    }

    /**
     * 绑定sql语句属性值
     *
     * @param stmt        sql语句对象
     * @param expressions 绑定条件值
     */
    public static void bindPropertyValue(SupportSQLiteProgram stmt, List<Expression> expressions) {
        if (expressions == null || expressions.isEmpty()) {
            return;
        }

        int bindIndex = 0;
        for (Expression item : expressions) {
            switch (item.expressionType) {
                case between:
                    if (item.value.length != 2) {
                        throw new IllegalArgumentException("'between...and...' The number of bound values does not match.");
                    }
                    SQLiteHelper.bindPropertyValue(item.property, stmt, ++bindIndex, item.value[0]);
                    SQLiteHelper.bindPropertyValue(item.property, stmt, ++bindIndex, item.value[1]);
                    break;
                case equal:
                case notEqual:
                case less:
                case lessOrEqual:
                case greater:
                case greaterOrEqual:
                case like:
                case contains:
                case startWith:
                case endWith:
                case regExp:
                    SQLiteHelper.bindPropertyValue(item.property, stmt, ++bindIndex, item.value[0]);
                    break;
                case in:
                case notIn:
                    for (Object value : item.value) {
                        SQLiteHelper.bindPropertyValue(item.property, stmt, ++bindIndex, value);
                    }
                    break;
                case isNull:
                case notNull:
                    break;
            }
        }
    }

    /**
     * 绑定sql语句属性值
     *
     * @param property  属性对象
     * @param stmt      sql语句对象
     * @param bindIndex 绑定位置(从1开始)
     * @param value     绑定值
     */
    public static void bindPropertyValue(Property property, SupportSQLiteProgram stmt, int bindIndex, Object value) {
        if (value == null) {
            stmt.bindNull(bindIndex);
            return;
        }

        switch (property.type.getName()) {
            case "java.lang.Boolean":
            case "boolean":
                stmt.bindLong(bindIndex, Boolean.parseBoolean(value.toString()) ? 1 : 0);
                break;
            case "java.lang.Integer":
            case "int":
            case "java.lang.Long":
            case "long":
            case "java.lang.Short":
            case "short":
                stmt.bindLong(bindIndex, Long.parseLong(value.toString()));
                break;
            case "java.lang.Byte":
            case "byte":
                stmt.bindBlob(bindIndex, (byte[]) value);
                break;
            case "java.lang.Float":
            case "float":
            case "java.lang.Double":
            case "double":
                stmt.bindDouble(bindIndex, Double.parseDouble(value.toString()));
                break;
            default:
                stmt.bindString(bindIndex, value.toString());
        }
    }

    /**
     * 执行删除sql语句
     *
     * @param db        RoomDatabase
     * @param deleteSQL 删除语句对象
     * @return 影响行数
     */
    public static int execDeleteSql(RoomDatabase db, DeleteSQL<?> deleteSQL) {
        db.assertNotSuspendingTransaction();

        final String sql = SQLiteHelper.generateDeleteSql(deleteSQL);
        final List<Expression> argumentList = deleteSQL.getArgumentList();

        if (RoomOperator.PRINT_SQL_LOG) {
            logSql(sql, argumentList);
        }

        final SupportSQLiteStatement stmt = db.compileStatement(sql);
        SQLiteHelper.bindPropertyValue(stmt, argumentList);

        db.beginTransaction();
        try {
            final int result = stmt.executeUpdateDelete();
            db.setTransactionSuccessful();
            return result;
        } finally {
            db.endTransaction();
        }
    }

    /**
     * 执行更新sql语句
     *
     * @param db        RoomDatabase
     * @param updateSQL 更新语句对象
     * @return 影响行数
     */
    public static <T> int execUpdateSql(RoomDatabase db, UpdateSQL<?> updateSQL) {
        db.assertNotSuspendingTransaction();

        final String sql = updateSQL.getSql();
        final List<Expression> argumentList = updateSQL.getArgumentList();

        if (RoomOperator.PRINT_SQL_LOG) {
            logSql(sql, argumentList);
        }

        final SupportSQLiteStatement stmt = db.compileStatement(sql);
        SQLiteHelper.bindPropertyValue(stmt, argumentList);

        db.beginTransaction();
        try {
            final int result = stmt.executeUpdateDelete();
            db.setTransactionSuccessful();
            return result;
        } finally {
            db.endTransaction();
        }
    }

    /**
     * 执行查询sql语句
     *
     * @param db        RoomDatabase
     * @param selectSQL 查询语句对象
     * @return 结果集
     */
    public static <T> List<T> execQuerySql(RoomDatabase db, SelectSQL<?> selectSQL) {
        return SQLiteHelper.execQuerySql(db, selectSQL, false);
    }

    /**
     * 执行查询sql语句
     *
     * @param db        RoomDatabase
     * @param selectSQL 查询语句对象
     * @return 结果
     */
    public static <T> T execQuerySqlReturnOne(RoomDatabase db, SelectSQL<?> selectSQL) {
        List<T> data = SQLiteHelper.execQuerySql(db, selectSQL, true);

        if (data == null || data.isEmpty()) {
            return null;
        }
        return data.get(0);
    }

    /**
     * 执行查询sql语句
     *
     * @param db          RoomDatabase
     * @param selectSQL   查询语句对象
     * @param isResultOne 是否只获取一条数据
     * @return 结果集
     */
    private static <T> List<T> execQuerySql(RoomDatabase db, SelectSQL<?> selectSQL, boolean isResultOne) {
        final String sql = selectSQL.getSql();
        final List<Expression> argumentList = selectSQL.getArgumentList();

        if (RoomOperator.PRINT_SQL_LOG) {
            logSql(sql, argumentList);
        }

        final RoomSQLiteQuery statement = RoomSQLiteQuery.acquire(sql, selectSQL.getArgumentCount());

        //绑定sql where 条件值
        if (selectSQL.where != null) {
            if (argumentList != null && !argumentList.isEmpty()) {
                SQLiteHelper.bindPropertyValue(statement, argumentList);
            }
        }

        //查询列
        final List<Property> selectColumns = selectSQL.getColumns();

        //是否属于指定获取单列
        final boolean isSingleColumn = selectSQL.isSingleColumn();

        db.assertNotSuspendingTransaction();
        try (Cursor cursor = DBUtil.query(db, statement, false, null)) {

            ArrayMap<String, Integer> columnIndexMap = null;

            if (selectSQL.function == null) {
                columnIndexMap = new ArrayMap<>();
                String field;
                for (Property item : selectColumns) {
                    field = Utils.getStrDefault(item.getAlias(), item.name);
                    columnIndexMap.put(field, CursorUtil.getColumnIndexOrThrow(cursor, field));
                }
            }

            final List<Object> result = new ArrayList<>();
            ArrayMap<String, Object> itemMap = null;
            Object value;
            String resultMapKey;

            while (cursor.moveToNext()) {
                if (!isSingleColumn) {
                    itemMap = new ArrayMap<>();
                }

                for (Property p : selectColumns) {
                    Integer index = columnIndexMap != null ? columnIndexMap.get(Utils.getStrDefault(p.getAlias(), p.name)) : null;
                    if (index == null) {
                        continue;
                    }

                    //获取map结果集key
                    resultMapKey = Utils.getStrDefault(p.getAlias(), p.javaName);

                    if (cursor.isNull(index)) {
                        if (!isSingleColumn) {
                            itemMap.put(resultMapKey, null);
                        }
                        continue;
                    }

                    switch (p.type.getName()) {
                        case "java.lang.Boolean":
                        case "boolean":
                        case "java.lang.Integer":
                        case "int":
                            value = cursor.getInt(index);
                            break;
                        case "java.lang.Long":
                        case "long":
                            value = cursor.getLong(index);
                            break;
                        case "java.lang.Byte":
                        case "byte":
                            value = cursor.getBlob(index);
                            break;
                        case "java.lang.Short":
                        case "short":
                            value = cursor.getShort(index);
                            break;
                        case "java.lang.Float":
                        case "float":
                            value = cursor.getFloat(index);
                            break;
                        case "java.lang.Double":
                        case "double":
                            value = cursor.getDouble(index);
                            break;
                        default:
                            value = cursor.getString(index);
                    }

                    if (isSingleColumn) {
                        result.add(value);
                    } else {
                        itemMap.put(resultMapKey, value);
                    }
                }

                if (!isSingleColumn) {
                    result.add(itemMap);
                }

                if (isResultOne) {
                    break;
                }
            }

            return JSON.parseArray(JSON.toJSONString(result), selectSQL.getResultType());
        } finally {
            statement.release();
        }
    }

    /**
     * 生成where 子句
     *
     * @param condition  条件列表
     * @param sqlBuilder sql构造器
     */
    private static void generateWhereSql(Condition condition, StringBuilder sqlBuilder) {
        if (condition == null) {
            return;
        }

        if (!condition.isConditionEmpty()) {
            for (int j = 0; j < condition.expressions.size(); j++) {
                Expression expression = condition.expressions.get(j);
                if (j > 0) {
                    sqlBuilder.append(" ").append(condition.whereRelation().name()).append(" ");
                }

                if (expression.isValueProperty) {
                    SQLiteHelper.handleValuePropertyCondition(expression, sqlBuilder);
                    continue;
                }

                if (expression.leftChildSelectSQL != null) {
                    sqlBuilder.append("(").append(expression.leftChildSelectSQL.getSql()).append(") ");
                } else if (expression.property != null) {
                    sqlBuilder
                            .append(expression.property.tableName)
                            .append(".")
                            .append(expression.property)
                            .append(" ");
                }

                switch (expression.expressionType) {
                    case equal:
                    case notEqual:
                    case less:
                    case lessOrEqual:
                    case greater:
                    case greaterOrEqual:
                    case like:
                    case regExp:
                    case contains:
                    case startWith:
                    case endWith:
                        if (expression.rightChildSelectSQL != null) {
                            sqlBuilder
                                    .append(String.format(expression.expressionType.getFormat()
                                            , String.format("(%s) ", expression.rightChildSelectSQL.getSql())))
                                    .append(" ");
                        } else {
                            sqlBuilder.append(expression.expressionType.getExp())
                                    .append(" ");
                        }
                        break;
                    case between:
                    case isNull:
                    case notNull:
                        sqlBuilder.append(expression.expressionType.getExp())
                                .append(" ");
                        break;
                    case in:
                    case notIn:
                        if (expression.rightChildSelectSQL != null) {
                            sqlBuilder
                                    .append(String.format(expression.expressionType.getFormat()
                                            , expression.rightChildSelectSQL.getSql()))
                                    .append(" ");
                        } else {
                            sqlBuilder
                                    .append(expression.expressionType.getExp())
                                    .append("(");
                            StringUtil.appendPlaceholders(sqlBuilder, expression.value.length);
                            sqlBuilder.append(") ");
                        }
                        break;
                    case exists:
                    case notExists:
                        if (expression.rightChildSelectSQL != null) {
                            sqlBuilder
                                    .append(expression.expressionType.getExp())
                                    .append(" ")
                                    .append("(")
                                    .append(expression.rightChildSelectSQL.getSql())
                                    .append(") ")
                                    .append(" ");
                        }
                }
            }
        }

        if (!condition.isWhereEmpty()) {
            Condition childCondition;
            for (int i = 0; i < condition.conditionList.size(); i++) {
                childCondition = condition.conditionList.get(i);
                if (i > 0 || !condition.isConditionEmpty()) {
                    sqlBuilder.append(" ").append(childCondition.whereRelation().name());
                }

                if (!childCondition.isWhereEmpty() && !childCondition.isConditionEmpty()) {
                    sqlBuilder.append(" ( ");
                } else {
                    sqlBuilder.append(" ");
                }

                SQLiteHelper.generateWhereSql(childCondition, sqlBuilder);

                if (!childCondition.isWhereEmpty() && !childCondition.isConditionEmpty()) {
                    sqlBuilder.append(" ) ");
                }
            }
        }
    }

    /**
     * 处理条件中value属于Property对象的条件
     *
     * @param expression 条件
     * @param sqlBuilder sqlBuilder
     */
    private static void handleValuePropertyCondition(Expression expression, StringBuilder sqlBuilder) {
        if (!expression.isValueProperty) {
            return;
        }

        Property value;

        switch (expression.expressionType) {
            case equal:
            case notEqual:
            case less:
            case lessOrEqual:
            case greater:
            case greaterOrEqual:
            case like:
            case regExp:
            case contains:
            case startWith:
            case endWith:
                value = (Property) expression.value[0];
                sqlBuilder
                        .append(expression.property.tableName)
                        .append(".")
                        .append(expression.property)
                        .append(" ")
                        .append(String.format(expression.expressionType.getFormat(), String.format("%s.%s", value.tableName, value.name)))
                        .append(" ");
                break;
            case between:
                value = (Property) expression.value[0];
                Property value2 = (Property) expression.value[1];
                sqlBuilder
                        .append(expression.property.tableName)
                        .append(".")
                        .append(expression.property)
                        .append(" ")
                        .append(String.format(
                                expression.expressionType.getFormat()
                                , String.format("%s.%s", value.tableName, value.name)
                                , String.format("%s.%s", value2.tableName, value2.name)
                        ))
                        .append(" ");
                break;
            case isNull:
            case notNull:
                //无需处理
                break;
            case in:
            case notIn:
                StringBuilder content = new StringBuilder();
                for (Object item : expression.value) {
                    if (item instanceof Property) {
                        Property p = (Property) item;
                        content.append(p.tableName).append(".").append(p.name).append(", ");
                        continue;
                    }

                    switch (expression.property.type.getName()) {
                        case "java.lang.Boolean":
                        case "boolean":
                            content.append(Boolean.parseBoolean(item.toString()) ? 1 : 0).append(",");
                            break;
                        case "java.lang.Integer":
                        case "int":
                        case "java.lang.Long":
                        case "long":
                        case "java.lang.Short":
                        case "short":
                        /*case "java.lang.Byte":
                        case "byte":*/
                        case "java.lang.Float":
                        case "float":
                        case "java.lang.Double":
                        case "double":
                            content.append(item).append(",");
                            break;
                        default:
                            content.append("'").append(item).append("'").append(",");
                    }
                }

                String result = null;
                if (content.length() > 0) {
                    result = content.substring(0, content.length() - 1);
                }

                sqlBuilder
                        .append(expression.property.tableName)
                        .append(".")
                        .append(expression.property)
                        .append(" ")
                        .append(String.format(expression.expressionType.getFormat(), result))
                        .append(" ");

                break;
        }
    }


    /**
     * 生成根据主键查询的where条件sql语句
     *
     * @param entity     实体信息
     * @param idInCount  根据主键id in 查询参数数量（仅单主键id有效，复合主键无效）
     * @param sqlBuilder sql构造
     */
    private static <T> void generatePKWhere(Entity<T> entity, int idInCount, StringBuilder sqlBuilder) {
        if (entity.getIdProperty() != null) {
            sqlBuilder.append(entity.getIdProperty());
            if (idInCount > 1) {
                sqlBuilder.append(" in(");
                StringUtil.appendPlaceholders(sqlBuilder, idInCount);
                sqlBuilder.append(") ");
            } else {
                sqlBuilder.append(" = ? ");
            }
            return;
        }

        if (entity.getCompositePK() != null) {
            for (Property p : entity.getCompositePK()) {
                sqlBuilder.append(" ").append(p).append(" = ? ");
            }
        }
    }

    /**
     * 生成获取EntityInsertionAdapter
     *
     * @param db                 RoomDatabase
     * @param entity             实体信息
     * @param onConflictStrategy insert 策略(详见OnConflictStrategy)
     */
    private static <T> EntityInsertionAdapter<T> newEntityInsertionAdapter(RoomDatabase db, Entity<T> entity, int onConflictStrategy) {
        final String sql = SQLiteHelper.generateInsertSql(entity, onConflictStrategy);
        return new EntityInsertionAdapter<T>(db) {
            @NonNull
            @Override
            public String createQuery() {
                return sql;
            }

            @Override
            public void bind(SupportSQLiteStatement stmt, T data) {
                entity.bindInsertionAdapterValue(stmt, data);
            }
        };
    }

    /**
     * 生成获取delete adapter
     *
     * @param db     RoomDatabase
     * @param entity 实体信息
     */
    private static <T> EntityDeletionOrUpdateAdapter<T> newEntityDeleteAdapter(RoomDatabase db, Entity<T> entity) {
        final String sql = SQLiteHelper.generatePKDeleteSql(entity, 1);
        return new EntityDeletionOrUpdateAdapter<T>(db) {
            @NonNull
            @Override
            public String createQuery() {
                return sql;
            }

            @Override
            public void bind(@NonNull SupportSQLiteStatement stmt, T value) {
                entity.bindDeleteAdapterValue(stmt, value);
            }
        };
    }

    /**
     * 生成获取update adapter
     *
     * @param db                 RoomDatabase
     * @param entity             实体信息
     * @param onConflictStrategy insert 策略(详见OnConflictStrategy)
     */
    private static <T> EntityDeletionOrUpdateAdapter<T> newEntityUpdateAdapter(RoomDatabase db, Entity<T> entity, int onConflictStrategy) {
        final String sql = SQLiteHelper.generatePKUpdateSql(entity, onConflictStrategy);
        return new EntityDeletionOrUpdateAdapter<T>(db) {
            @NonNull
            @Override
            public String createQuery() {
                return sql;
            }

            @Override
            public void bind(@NonNull SupportSQLiteStatement stmt, T value) {
                entity.bindUpdateAdapterValue(stmt, value);
            }
        };
    }

    /**
     * 打印sql日志
     *
     * @param sql            原始sql语句
     * @param expressionList sql 参数列表
     */
    private static void logSql(final String sql, List<Expression> expressionList) {
        Log.d(TAG, "原始SQL: " + sql);

        if (expressionList == null || expressionList.isEmpty()) {
            return;
        }

        String logSql = sql;
        for (Expression item : expressionList) {
            if (item.value == null || item.value.length == 0) {
                continue;
            }
            for (Object obj : item.value) {
                switch (item.property.type.getName()) {
                    case "java.lang.Boolean":
                    case "boolean":
                        logSql = logSql.replaceFirst("\\?", Boolean.parseBoolean(obj.toString()) ? "1" : "0");
                        break;
                    case "java.lang.Integer":
                    case "int":
                    case "java.lang.Long":
                    case "long":
                    case "java.lang.Short":
                    case "short":
                    case "java.lang.Byte":
                    case "byte":
                    case "java.lang.Float":
                    case "float":
                    case "java.lang.Double":
                    case "double":
                        logSql = logSql.replaceFirst("\\?", obj.toString());
                        break;
                    default:
                        logSql = logSql.replaceFirst("\\?", String.format("'%s'", obj.toString()));
                }

            }
        }

        Log.d(TAG, "日志SQL: " + logSql);
    }
}
