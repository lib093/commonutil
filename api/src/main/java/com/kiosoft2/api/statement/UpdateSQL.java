package com.kiosoft2.api.statement;

import com.kiosoft2.api.Entity;
import com.kiosoft2.api.helper.SQLiteHelper;
import com.kiosoft2.api.statement.element.Condition;
import com.kiosoft2.api.statement.element.Expression;
import com.kiosoft2.api.type.OpType;

import java.util.ArrayList;
import java.util.List;

/**
 
 * @createDate: 2023/8/15 17:43
 * @version: 1.0
 * @description: update sql对象
 */
public class UpdateSQL<T> extends SQL<T>{

    /**set 字段列表*/
    public final List<Expression> setFields = new ArrayList<>();

    /**where条件*/
    public Condition where;

    public UpdateSQL(Entity<T> from) {
        super(OpType.update,from);
    }

    @Override
    public int getArgumentCount() {
        int argumentCount = setFields.size();
        if(where != null){
            argumentCount += where.getArgumentCount();
        }
        return argumentCount;
    }

    @Override
    public List<Expression> getArgumentList() {
        List<Expression> result = new ArrayList<>();
        if(!setFields.isEmpty()){
            result.addAll(setFields);
        }
        if(where != null){
            result.addAll(where.getConditionList());
        }
        return result;
    }

    @Override
    public String getSql() {
       return SQLiteHelper.generateUpdateSql(this);
    }
}
