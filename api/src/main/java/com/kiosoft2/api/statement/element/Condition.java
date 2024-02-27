package com.kiosoft2.api.statement.element;

import com.kiosoft2.api.Property;
import com.kiosoft2.api.type.ConditionRelation;
import com.kiosoft2.api.type.ExpressionType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 
 * @createDate: 2023/8/12 15:04
 * @version: 1.0
 * @description: 条件对象
 */
public class Condition {

    /**条件关系*/
    private ConditionRelation conditionRelation;

    /**表达式对象列表*/
    public final List<Expression> expressions;

    /**条件组列表*/
    public final List<Condition> conditionList;

    /**是否属于子条件组*/
    public final boolean isChildCondition;

    public Condition(ConditionRelation conditionRelation, List<Expression> expressions) {
        this.conditionRelation = conditionRelation;
        this.expressions = expressions;
        this.conditionList = null;
        this.isChildCondition = false;
    }

    public Condition(ConditionRelation conditionRelation, List<Expression> expressions, List<Condition> conditionList) {
        this.conditionRelation = conditionRelation;
        this.expressions = expressions;
        this.conditionList = conditionList;
        this.isChildCondition = false;
    }

    private Condition(ConditionRelation conditionRelation, List<Expression> expressions, List<Condition> conditionList, boolean isChildCondition) {
        this.conditionRelation = conditionRelation;
        this.expressions = expressions;
        this.conditionList = conditionList;
        this.isChildCondition = isChildCondition;
    }

    /**
     * 创建新where子条件对象
     */
    public static Condition newChildWhere(){
        return new Condition(ConditionRelation.and,new ArrayList<>(),new ArrayList<>(),true);
    }

    /**
     * 获取匹配主键id的条件
     * @param id    主键id属性
     * @param value id值
     * @return      条件
     */
    public static Condition equalId(Property id, Object value){
        if(!id.isId){
            throw new IllegalArgumentException(String.format("The %s field is not a primary key",id));
        }
        return new Condition(
                ConditionRelation.and
                , Collections.singletonList(new Expression(id, ExpressionType.equal, new Object[]{value})));
    }

    /**
     * 获取匹配批量主键id的条件
     * @param id    主键id数组
     * @param value id值
     * @return      条件
     */
    public static Condition inId(Property id, Object[] value){
        if(!id.isId){
            throw new IllegalArgumentException(String.format("The %s field is not a primary key",id));
        }
        return new Condition(
                ConditionRelation.and
                , Collections.singletonList(new Expression(id, ExpressionType.in, value)));
    }

    /**
     * 获取where条件关系
     */
    public ConditionRelation whereRelation() {
        return conditionRelation;
    }

    /**
     * 应用新的where条件关系
     */
    public void applyWhereRelation(ConditionRelation conditionRelation){
        this.conditionRelation = conditionRelation;
    }

    /**
     * 是否属于子where条件
     */
    public boolean isChildCondition() {
        return isChildCondition;
    }

    /**
     * 添加条件对象
     */
    public void add(Expression expression){
        this.expressions.add(expression);
    }

    /**
     * 添加where对象
     */
    public void addWhere(Condition condition){
        this.conditionList.add(condition);
    }

    /**
     * 条件列表是否为空
     */
    public boolean isConditionEmpty(){
        return this.expressions == null || this.expressions.isEmpty();
    }

    /**
     * where列表是否为空
     */
    public boolean isWhereEmpty(){
        return this.conditionList == null || this.conditionList.isEmpty();
    }

    /**
     * where 条件是否有效
     */
    public boolean isValid(){
        return !this.isConditionEmpty() || !this.isWhereEmpty();
    }

    /**
     * 获取收集Condition列表
     */
    public List<Expression> getConditionList() {
        return this.conditionList(this);
    }

    /**
     * 获取sql参数数量
     */
    public int getArgumentCount() {
        return this.argumentCount(this);
    }

    /**
     * 收集Condition列表
     *
     * @param condition 条件
     * @return Condition列表
     */
    private List<Expression> conditionList(Condition condition) {
        if(condition == null){
            return null;
        }

        List<Expression> result = new ArrayList<>();
        if(condition.expressions != null && !condition.expressions.isEmpty() ){
            for(Expression c : condition.expressions){
                if(c.isValueProperty){
                    continue;
                }

                if(c.leftChildSelectSQL != null){
                    result.addAll(c.leftChildSelectSQL.getArgumentList());
                }

                if(c.rightChildSelectSQL != null){
                    result.addAll(c.rightChildSelectSQL.getArgumentList());
                    continue;
                }

                result.add(c);
            }
        }

        if(condition.conditionList != null && !condition.conditionList.isEmpty()){
            List<Expression> list;
            for(Condition item : condition.conditionList){
                list = this.conditionList(item);
                if(list == null || list.isEmpty()){
                    continue;
                }

                result.addAll(list);
            }
        }

        return result;
    }

    /**
     * 合计sql参数数量
     *
     * @param condition 条件列表
     * @return 合计参数数量
     */
    private int argumentCount(Condition condition) {
        if(condition == null){
            return 0;
        }

        int argumentCount = 0;
        if(condition.expressions != null && !condition.expressions.isEmpty()){
            for (Expression expression : condition.expressions) {
                if(expression.isValueProperty){
                    continue;
                }

                if(expression.leftChildSelectSQL != null){
                    argumentCount += expression.leftChildSelectSQL.getArgumentCount();
                }

                if(expression.rightChildSelectSQL != null){
                    argumentCount += expression.rightChildSelectSQL.getArgumentCount();
                    continue;
                }

                switch (expression.expressionType) {
                    case between:
                        argumentCount += 2;
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
                        argumentCount++;
                        break;
                    case in:
                    case notIn:
                        if(expression.value == null || expression.value.length == 0){
                            throw new IllegalArgumentException(String.format("%s statement parameter error.", expression.expressionType));
                        }
                        argumentCount += expression.value.length;
                        break;
                    case isNull:
                    case notNull:
                        break;
                }
            }
        }

        if (condition.conditionList != null && !condition.conditionList.isEmpty()) {
            for(Condition item : condition.conditionList){
                argumentCount += this.argumentCount(item);
            }
        }

        return argumentCount;
    }
}
