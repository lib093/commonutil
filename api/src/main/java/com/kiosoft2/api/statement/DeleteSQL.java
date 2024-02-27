package com.kiosoft2.api.statement;

import com.kiosoft2.api.Entity;
import com.kiosoft2.api.helper.SQLiteHelper;
import com.kiosoft2.api.statement.element.Condition;
import com.kiosoft2.api.statement.element.Expression;
import com.kiosoft2.api.type.OpType;

import java.util.List;

/**
 
 * @createDate: 2023/8/15 17:43
 * @version: 1.0
 * @description: delete sql对象
 */
public class DeleteSQL<T> extends SQL<T>{

    /**where条件*/
    public Condition where;

    public DeleteSQL(Entity<T> from) {
        super(OpType.delete,from);
    }

    @Override
    public int getArgumentCount() {
        if(where == null){
            return 0;
        }
        return where.getArgumentCount();
    }

    @Override
    public List<Expression> getArgumentList() {
        if(where == null){
            return null;
        }
        return where.getConditionList();
    }

    @Override
    public String getSql() {
       return SQLiteHelper.generateDeleteSql(this);
    }
}
