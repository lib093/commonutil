package com.kiosoft2.api.builder;

import androidx.annotation.NonNull;
import androidx.room.RoomDatabase;

import com.kiosoft2.api.Property;
import com.kiosoft2.api.helper.SQLiteHelper;
import com.kiosoft2.api.statement.SelectSQL;
import com.kiosoft2.api.statement.element.Function;
import com.kiosoft2.api.statement.element.GroupBy;
import com.kiosoft2.api.statement.element.OrderBy;
import com.kiosoft2.api.statement.element.Page;
import com.kiosoft2.api.type.FuncType;
import com.kiosoft2.api.type.OrderByType;

import java.util.Collections;
import java.util.List;

/**
 
 * @createDate: 2023/8/13 15:39
 * @version: 1.0
 * @description: 查询
 */
public class Query<T> {

    /**数据库操作*/
    private final RoomDatabase db;

    /**select sql对象*/
    private final SelectSQL<T> sql;


    public Query(RoomDatabase db, SelectSQL<T> sql) {
        this.db = db;
        this.sql = sql;
    }

    //--------------------------------------------find----------------------------------------------

    /**
     * 开始查询
     * @return 结果集
     */
    public List<T> find(){
        return SQLiteHelper.execQuerySql(db,sql);
    }

    /**
     * 开始查询
     * @param resultClass 结果集元素class
     * @return 结果集
     */
    public List<T> find(Class<?> resultClass){
        this.sql.setResultType(resultClass);
        return SQLiteHelper.execQuerySql(db,sql);
    }

    /**
     * 开始查询
     * @return 结果集
     */
    public T findFirst(){
        this.limit(1).offset(0);
        return SQLiteHelper.execQuerySqlReturnOne(db,sql);
    }

    /**
     * 开始查询
     * @param resultClass 结果集元素class
     * @return 结果集
     */
    public T findFirst(Class<?> resultClass){
        this.limit(1).offset(0);
        this.sql.setResultType(resultClass);
        return SQLiteHelper.execQuerySqlReturnOne(db,sql);
    }

    /**
     * 仅查询出主键id列表
     */
    public List<String> findIds(){
        this.sql.setColumns(Collections.singletonList(sql.from.getIdProperty()));
        this.sql.setResultType(sql.from.getIdProperty().type);
        return SQLiteHelper.execQuerySql(db,sql);
    }

    /**
     * 仅查询出主键id
     */
    public String findFirstId(){
        this.sql.setColumns(Collections.singletonList(sql.from.getIdProperty()));
        this.sql.setResultType(sql.from.getIdProperty().type);
        this.limit(1).offset(0);
        return SQLiteHelper.execQuerySqlReturnOne(db,sql);
    }

    //--------------------------------------------聚合函数--------------------------------------------

    public long count(){
        this.sql.function = new Function(FuncType.count,null);
        this.sql.setResultType(Long.class);

        Long result = SQLiteHelper.execQuerySqlReturnOne(db,sql);
        return result != null ? result : 0;
    }

    public double max(@NonNull Property property){
        this.sql.function = new Function(FuncType.max,property);
        this.sql.setResultType(double.class);

        Double result = SQLiteHelper.execQuerySqlReturnOne(db,sql);
        return result != null ? result : 0;
    }

    public double min(@NonNull Property property){
        this.sql.function = new Function(FuncType.min,property);
        this.sql.setResultType(double.class);

        Double result = SQLiteHelper.execQuerySqlReturnOne(db,sql);
        return result != null ? result : 0;
    }

    public double avg(@NonNull Property property){
        this.sql.function = new Function(FuncType.avg,property);
        this.sql.setResultType(double.class);

        Double result = SQLiteHelper.execQuerySqlReturnOne(db,sql);
        return result != null ? result : 0;
    }

    public double sum(@NonNull Property property){
        this.sql.function = new Function(FuncType.sum,property);
        this.sql.setResultType(double.class);

        Double result = SQLiteHelper.execQuerySqlReturnOne(db,sql);
        return result != null ? result : 0;
    }

    //----------------------------------------------------------------------------------------------

    public Query<T> distinct(){
        this.sql.distinct = true;
        return this;
    }

    public Query<T> groupBy(Property... property){
        if(property == null || property.length == 0){
            return this;
        }
        if(sql.groupBy == null){
            sql.groupBy = new GroupBy();
        }
        sql.groupBy.property = property;
        return this;
    }

    public Query<T> orderBy(Property property){
        if(sql.orderBy == null){
            sql.orderBy = new OrderBy();
        }
        sql.orderBy.property = new Property[]{property};
        return this;
    }

    public Query<T> orderBy(Property property,OrderByType type){
        if(sql.orderBy == null){
            sql.orderBy = new OrderBy();
        }
        sql.orderBy.property = new Property[]{property};
        sql.orderBy.type = type;
        return this;
    }

    public Query<T> orderBy(Property[] properties,OrderByType type){
        if(sql.orderBy == null){
            sql.orderBy = new OrderBy();
        }
        sql.orderBy.property = properties;
        sql.orderBy.type = type;
        return this;
    }

    public Query<T> limit(int limit){
        if(sql.page == null){
            sql.page = new Page();
        }
        sql.page.limit = limit;
        return this;
    }

    public Query<T> offset(int offset){
        if(sql.page == null){
            sql.page = new Page();
        }
        sql.page.offset = offset;
        return this;
    }

    public Query<T> page(int pageSize,int pageNo){
        if(sql.page == null){
            sql.page = new Page();
        }
        sql.page.limit = pageSize;
        sql.page.offset = pageSize * (pageNo - 1);
        return this;
    }
}
