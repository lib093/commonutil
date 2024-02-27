package com.kiosoft2.api.builder;

import androidx.annotation.NonNull;

import com.kiosoft2.api.Property;
import com.kiosoft2.api.statement.SelectSQL;
import com.kiosoft2.api.statement.element.Function;
import com.kiosoft2.api.statement.element.OrderBy;
import com.kiosoft2.api.statement.element.Page;
import com.kiosoft2.api.type.FuncType;
import com.kiosoft2.api.type.OrderByType;

/**
 
 * @createDate: 2023/8/13 15:39
 * @version: 1.0
 * @description: 子查询
 */
public class ChildQuery {

    /**select sql对象*/
    private final SelectSQL<?> sql;


    public ChildQuery(SelectSQL<?> sql) {
        this.sql = sql;
    }

    //--------------------------------------------find----------------------------------------------

    /**
     * 获取查询SQL对象
     */
    public SelectSQL<?> getSql(){
        return sql;
    }

    //--------------------------------------------聚合函数--------------------------------------------

    public ChildQuery count(){
        this.sql.function = new Function(FuncType.count,null);
        return this;
    }

    public ChildQuery max(@NonNull Property property){
        this.sql.function = new Function(FuncType.max,property);
        return this;
    }

    public ChildQuery min(@NonNull Property property){
        this.sql.function = new Function(FuncType.min,property);
        return this;
    }

    public ChildQuery avg(@NonNull Property property){
        this.sql.function = new Function(FuncType.avg,property);
        return this;
    }

    public ChildQuery sum(@NonNull Property property){
        this.sql.function = new Function(FuncType.sum,property);
        return this;
    }

    //----------------------------------------------------------------------------------------------

    public ChildQuery orderBy(Property property){
        if(sql.orderBy == null){
            sql.orderBy = new OrderBy();
        }
        sql.orderBy.property = new Property[]{property};
        return this;
    }

    public ChildQuery orderBy(Property property, OrderByType type){
        if(sql.orderBy == null){
            sql.orderBy = new OrderBy();
        }
        sql.orderBy.property = new Property[]{property};
        sql.orderBy.type = type;
        return this;
    }

    public ChildQuery orderBy(Property[] properties, OrderByType type){
        if(sql.orderBy == null){
            sql.orderBy = new OrderBy();
        }
        sql.orderBy.property = properties;
        sql.orderBy.type = type;
        return this;
    }

    public ChildQuery limit(int limit){
        if(sql.page == null){
            sql.page = new Page();
        }
        sql.page.limit = limit;
        return this;
    }

    public ChildQuery offset(int offset){
        if(sql.page == null){
            sql.page = new Page();
        }
        sql.page.offset = offset;
        return this;
    }

    public ChildQuery page(int pageSize, int pageNo){
        if(sql.page == null){
            sql.page = new Page();
        }
        sql.page.limit = pageSize;
        sql.page.offset = pageSize * (pageNo - 1);
        return this;
    }
}
