package com.kiosoft2.api.builder;

import com.kiosoft2.api.condition.BasicCondition;
import com.kiosoft2.api.condition.PropertyCondition;
import com.kiosoft2.api.statement.element.Condition;
import com.kiosoft2.api.type.ConditionRelation;

import java.util.ArrayList;

/**
 
 * @createDate: 2023/8/15 16:39
 * @version: 1.0
 * @description: 基础条件关系构造
 * E:实体泛型、T:子类泛型
 */
@SuppressWarnings("unchecked")
public abstract class BasicConditionRelationBuilder<E,T extends BasicConditionRelationBuilder<E,T>> extends BasicCondition<T> {

    /**
     * 当前主where条件
     */
    protected final Condition condition;


    public BasicConditionRelationBuilder() {
        this.condition = new Condition(ConditionRelation.and, new ArrayList<>(), new ArrayList<>());
        this.applyWhere(condition);
    }

    public T and() {
        this.applyWhere(new Condition(ConditionRelation.and, new ArrayList<>(), new ArrayList<>()));
        this.condition.addWhere(this.getApplyWhere());
        return (T)this;
    }

    public T and(PropertyCondition propertyCondition) {
        Condition childCondition = propertyCondition.getWhere();
        childCondition.applyWhereRelation(ConditionRelation.and);
        this.getApplyWhere().addWhere(childCondition);
        return (T)this;
    }

    public T or() {
        this.applyWhere(new Condition(ConditionRelation.or, new ArrayList<>(), new ArrayList<>()));
        this.condition.addWhere(this.getApplyWhere());
        return (T)this;
    }

    public T or(PropertyCondition propertyCondition) {
        Condition childCondition = propertyCondition.getWhere();
        childCondition.applyWhereRelation(ConditionRelation.or);
        this.getApplyWhere().addWhere(childCondition);
        return (T)this;
    }

}
