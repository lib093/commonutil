package com.kiosoft2.api.condition;

import com.kiosoft2.api.Property;
import com.kiosoft2.api.builder.ChildQuery;
import com.kiosoft2.api.statement.element.Condition;
import com.kiosoft2.api.statement.element.Expression;
import com.kiosoft2.api.type.ExpressionType;
import com.kiosoft2.api.type.ConditionRelation;

import java.util.ArrayList;

/**
 
 * @createDate: 2023/8/7 18:11
 * @version: 1.0
 * @description: where 查询条件
 */
public class PropertyCondition {

    /**
     * 主where条件
     */
    private final Condition condition;

    /**
     * 当前操作的where条件
     */
    private Condition applyCondition;


    public PropertyCondition(Property property, ExpressionType expressionType) {
        this.condition = Condition.newChildWhere();
        this.applyWhere(condition);
        this.getApplyWhere().add(new Expression(property, expressionType));
    }

    public PropertyCondition(Property property, ExpressionType expressionType, Object[] value) {
        this.condition = Condition.newChildWhere();
        this.applyWhere(condition);
        this.getApplyWhere().add(new Expression(property, expressionType, value));
    }

    public PropertyCondition(Property property, ExpressionType expressionType, ChildQuery childQuery) {
        this.condition = Condition.newChildWhere();
        this.applyWhere(condition);
        this.getApplyWhere().add(new Expression(property, expressionType, childQuery.getSql()));
    }

    /**
     * 获取where条件对象
     */
    public Condition getWhere() {
        return condition;
    }

    /**
     * 应用新的where对象
     */
    private void applyWhere(Condition condition) {
        this.applyCondition = condition;
    }

    /**
     * 获取设置好之后的applyWhere对象
     */
    private Condition getApplyWhere() {
        return applyCondition;
    }

    /**
     * and 条件
     */
    public PropertyCondition and(PropertyCondition propertyCondition) {
        this.applyWhere(new Condition(ConditionRelation.and, new ArrayList<>(), new ArrayList<>()));
        this.getApplyWhere().addWhere(propertyCondition.condition);
        this.condition.addWhere(this.getApplyWhere());
        return this;
    }

    /**
     * or 条件
     */
    public PropertyCondition or(PropertyCondition propertyCondition) {
        this.applyWhere(new Condition(ConditionRelation.or, new ArrayList<>(), new ArrayList<>()));
        this.getApplyWhere().addWhere(propertyCondition.condition);
        this.condition.addWhere(this.getApplyWhere());
        return this;
    }
}
