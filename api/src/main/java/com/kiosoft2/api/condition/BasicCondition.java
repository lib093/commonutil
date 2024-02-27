package com.kiosoft2.api.condition;

import com.kiosoft2.api.Property;
import com.kiosoft2.api.builder.ChildQuery;
import com.kiosoft2.api.statement.element.Condition;
import com.kiosoft2.api.statement.element.Expression;
import com.kiosoft2.api.type.ExpressionType;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

/**
 
 * @createDate: 2023/8/15 16:09
 * @version: 1.0
 * @description: 基础条件
 */
@SuppressWarnings("unchecked")
public abstract class BasicCondition<T extends BasicCondition<T>> {

    /**
     * 当前操作的where条件
     */
    private Condition applyCondition;

    /**
     * 应用新的当前操作的where条件
     */
    protected void applyWhere(Condition condition) {
        this.applyCondition = condition;
    }

    /**
     * 获取当前操作的where条件
     */
    protected Condition getApplyWhere() {
        return applyCondition;
    }

    //------------------------------------------exists----------------------------------------------

    public T exists(ChildQuery childQuery){
        applyCondition.add(new Expression(ExpressionType.exists,childQuery.getSql()));
        return (T)this;
    }

    public T notExists(ChildQuery childQuery){
        applyCondition.add(new Expression(ExpressionType.notExists,childQuery.getSql()));
        return (T)this;
    }

    //--------------------------------------isNull and notNull--------------------------------------

    public T isNull(Property property) {
        applyCondition.add(new Expression(property, ExpressionType.isNull));
        return (T)this;
    }

    public T notNull(Property property) {
        applyCondition.add(new Expression(property, ExpressionType.notNull));
        return (T)this;
    }

    //**********************************************************************************************

    public T isNull(ChildQuery childQuery) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.isNull));
        return (T)this;
    }

    public T notNull(ChildQuery childQuery) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.notNull));
        return (T)this;
    }

    //----------------------------------------equal(=)----------------------------------------------
    public T equal(Property property, long value) {
        applyCondition.add(new Expression(property, ExpressionType.equal, new Object[]{value}));
        return (T)this;
    }

    public T equal(Property property, double value) {
        applyCondition.add(new Expression(property, ExpressionType.equal, new Object[]{value}));
        return (T)this;
    }

    public T equal(Property property, byte[] value) {
        applyCondition.add(new Expression(property, ExpressionType.equal, new Object[]{value}));
        return (T)this;
    }

    public T equal(Property property, String value) {
        applyCondition.add(new Expression(property, ExpressionType.equal, new Object[]{value}));
        return (T)this;
    }

    public T equal(Property property, boolean value) {
        applyCondition.add(new Expression(property, ExpressionType.equal, new Object[]{value}));
        return (T)this;
    }

    public T equal(Property property, Date value) {
        applyCondition.add(new Expression(property, ExpressionType.equal, new Object[]{value.getTime()}));
        return (T)this;
    }

    //**********************************************************************************************

    public T equal(ChildQuery leftChildQuery, ChildQuery rightChildQuery) {
        applyCondition.add(new Expression(leftChildQuery.getSql(), ExpressionType.equal, rightChildQuery.getSql()));
        return (T)this;
    }

    public T equal(Property property, ChildQuery childQuery) {
        applyCondition.add(new Expression(property, ExpressionType.equal, childQuery.getSql()));
        return (T)this;
    }

    public T equal(ChildQuery childQuery, long value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.equal, new Object[]{value}));
        return (T)this;
    }

    public T equal(ChildQuery childQuery, double value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.equal, new Object[]{value}));
        return (T)this;
    }

    public T equal(ChildQuery childQuery, byte[] value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.equal, new Object[]{value}));
        return (T)this;
    }

    public T equal(ChildQuery childQuery, String value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.equal, new Object[]{value}));
        return (T)this;
    }

    public T equal(ChildQuery childQuery, boolean value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.equal, new Object[]{value}));
        return (T)this;
    }

    public T equal(ChildQuery childQuery, Date value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.equal, new Object[]{value.getTime()}));
        return (T)this;
    }

    //---------------------------------------notEqual(!=)-----------------------------------------------

    public T notEqual(Property property, long value) {
        applyCondition.add(new Expression(property, ExpressionType.notEqual, new Object[]{value}));
        return (T)this;
    }

    public T notEqual(Property property, double value) {
        applyCondition.add(new Expression(property, ExpressionType.notEqual, new Object[]{value}));
        return (T)this;
    }

    public T notEqual(Property property, byte[] value) {
        applyCondition.add(new Expression(property, ExpressionType.notEqual, new Object[]{value}));
        return (T)this;
    }

    public T notEqual(Property property, String value) {
        applyCondition.add(new Expression(property, ExpressionType.notEqual, new Object[]{value}));
        return (T)this;
    }

    public T notEqual(Property property, boolean value) {
        applyCondition.add(new Expression(property, ExpressionType.notEqual, new Object[]{value}));
        return (T)this;
    }

    public T notEqual(Property property, Date value) {
        applyCondition.add(new Expression(property, ExpressionType.notEqual, new Object[]{value.getTime()}));
        return (T)this;
    }

    //**********************************************************************************************

    public T notEqual(ChildQuery leftChildQuery, ChildQuery rightChildQuery) {
        applyCondition.add(new Expression(leftChildQuery.getSql(), ExpressionType.notEqual, rightChildQuery.getSql()));
        return (T)this;
    }

    public T notEqual(Property property, ChildQuery childQuery) {
        applyCondition.add(new Expression(property, ExpressionType.notEqual, childQuery.getSql()));
        return (T)this;
    }

    public T notEqual(ChildQuery childQuery, long value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.notEqual, new Object[]{value}));
        return (T)this;
    }

    public T notEqual(ChildQuery childQuery, double value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.notEqual, new Object[]{value}));
        return (T)this;
    }

    public T notEqual(ChildQuery childQuery, byte[] value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.notEqual, new Object[]{value}));
        return (T)this;
    }

    public T notEqual(ChildQuery childQuery, String value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.notEqual, new Object[]{value}));
        return (T)this;
    }

    public T notEqual(ChildQuery childQuery, boolean value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.notEqual, new Object[]{value}));
        return (T)this;
    }

    public T notEqual(ChildQuery childQuery, Date value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.notEqual, new Object[]{value.getTime()}));
        return (T)this;
    }

    //-----------------------------------------less(<)----------------------------------------------
    public T less(Property property, long value) {
        applyCondition.add(new Expression(property, ExpressionType.less, new Object[]{value}));
        return (T)this;
    }

    public T less(Property property, double value) {
        applyCondition.add(new Expression(property, ExpressionType.less, new Object[]{value}));
        return (T)this;
    }

    public T less(Property property, byte[] value) {
        applyCondition.add(new Expression(property, ExpressionType.less, new Object[]{value}));
        return (T)this;
    }

    public T less(Property property, String value) {
        applyCondition.add(new Expression(property, ExpressionType.less, new Object[]{value}));
        return (T)this;
    }

    public T less(Property property, Date value) {
        applyCondition.add(new Expression(property, ExpressionType.less, new Object[]{value.getTime()}));
        return (T)this;
    }

    //**********************************************************************************************

    public T less(ChildQuery leftChildQuery, ChildQuery rightChildQuery) {
        applyCondition.add(new Expression(leftChildQuery.getSql(), ExpressionType.less, rightChildQuery.getSql()));
        return (T)this;
    }

    public T less(Property property, ChildQuery childQuery) {
        applyCondition.add(new Expression(property, ExpressionType.less, childQuery.getSql()));
        return (T)this;
    }

    public T less(ChildQuery childQuery, long value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.less, new Object[]{value}));
        return (T)this;
    }

    public T less(ChildQuery childQuery, double value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.less, new Object[]{value}));
        return (T)this;
    }

    public T less(ChildQuery childQuery, byte[] value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.less, new Object[]{value}));
        return (T)this;
    }

    public T less(ChildQuery childQuery, String value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.less, new Object[]{value}));
        return (T)this;
    }

    public T less(ChildQuery childQuery, Date value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.less, new Object[]{value.getTime()}));
        return (T)this;
    }

    //----------------------------------------lessOrEqual(<=)-------------------------------------------------

    public T lessOrEqual(Property property, long value) {
        applyCondition.add(new Expression(property, ExpressionType.lessOrEqual, new Object[]{value}));
        return (T)this;
    }

    public T lessOrEqual(Property property, double value) {
        applyCondition.add(new Expression(property, ExpressionType.lessOrEqual, new Object[]{value}));
        return (T)this;
    }

    public T lessOrEqual(Property property, byte[] value) {
        applyCondition.add(new Expression(property, ExpressionType.lessOrEqual, new Object[]{value}));
        return (T)this;
    }

    public T lessOrEqual(Property property, String value) {
        applyCondition.add(new Expression(property, ExpressionType.lessOrEqual, new Object[]{value}));
        return (T)this;
    }

    public T lessOrEqual(Property property, Date value) {
        applyCondition.add(new Expression(property, ExpressionType.lessOrEqual, new Object[]{value.getTime()}));
        return (T)this;
    }

    //**********************************************************************************************

    public T lessOrEqual(ChildQuery leftChildQuery, ChildQuery rightChildQuery) {
        applyCondition.add(new Expression(leftChildQuery.getSql(), ExpressionType.lessOrEqual, rightChildQuery.getSql()));
        return (T)this;
    }

    public T lessOrEqual(Property property, ChildQuery childQuery) {
        applyCondition.add(new Expression(property, ExpressionType.lessOrEqual, childQuery.getSql()));
        return (T)this;
    }

    public T lessOrEqual(ChildQuery childQuery, long value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.lessOrEqual, new Object[]{value}));
        return (T)this;
    }

    public T lessOrEqual(ChildQuery childQuery, double value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.lessOrEqual, new Object[]{value}));
        return (T)this;
    }

    public T lessOrEqual(ChildQuery childQuery, byte[] value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.lessOrEqual, new Object[]{value}));
        return (T)this;
    }

    public T lessOrEqual(ChildQuery childQuery, String value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.lessOrEqual, new Object[]{value}));
        return (T)this;
    }

    public T lessOrEqual(ChildQuery childQuery, Date value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.lessOrEqual, new Object[]{value.getTime()}));
        return (T)this;
    }

    //----------------------------------------greater(>)-------------------------------------------------

    public T greater(Property property, long value) {
        applyCondition.add(new Expression(property, ExpressionType.greater, new Object[]{value}));
        return (T)this;
    }

    public T greater(Property property, double value) {
        applyCondition.add(new Expression(property, ExpressionType.greater, new Object[]{value}));
        return (T)this;
    }

    public T greater(Property property, byte[] value) {
        applyCondition.add(new Expression(property, ExpressionType.greater, new Object[]{value}));
        return (T)this;
    }

    public T greater(Property property, String value) {
        applyCondition.add(new Expression(property, ExpressionType.greater, new Object[]{value}));
        return (T)this;
    }

    public T greater(Property property, Date value) {
        applyCondition.add(new Expression(property, ExpressionType.greater, new Object[]{value.getTime()}));
        return (T)this;
    }

    //**********************************************************************************************

    public T greater(ChildQuery leftChildQuery, ChildQuery rightChildQuery) {
        applyCondition.add(new Expression(leftChildQuery.getSql(), ExpressionType.greater, rightChildQuery.getSql()));
        return (T)this;
    }

    public T greater(Property property, ChildQuery childQuery) {
        applyCondition.add(new Expression(property, ExpressionType.greater, childQuery.getSql()));
        return (T)this;
    }

    public T greater(ChildQuery childQuery, long value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.greater, new Object[]{value}));
        return (T)this;
    }

    public T greater(ChildQuery childQuery, double value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.greater, new Object[]{value}));
        return (T)this;
    }

    public T greater(ChildQuery childQuery, byte[] value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.greater, new Object[]{value}));
        return (T)this;
    }

    public T greater(ChildQuery childQuery, String value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.greater, new Object[]{value}));
        return (T)this;
    }

    public T greater(ChildQuery childQuery, Date value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.greater, new Object[]{value.getTime()}));
        return (T)this;
    }

    //-------------------------------greaterOrEqual(>=)---------------------------------------------

    public T greaterOrEqual(Property property, long value) {
        applyCondition.add(new Expression(property, ExpressionType.greaterOrEqual, new Object[]{value}));
        return (T)this;
    }

    public T greaterOrEqual(Property property, double value) {
        applyCondition.add(new Expression(property, ExpressionType.greaterOrEqual, new Object[]{value}));
        return (T)this;
    }

    public T greaterOrEqual(Property property, byte[] value) {
        applyCondition.add(new Expression(property, ExpressionType.greaterOrEqual, new Object[]{value}));
        return (T)this;
    }

    public T greaterOrEqual(Property property, String value) {
        applyCondition.add(new Expression(property, ExpressionType.greaterOrEqual, new Object[]{value}));
        return (T)this;
    }

    public T greaterOrEqual(Property property, Date value) {
        applyCondition.add(new Expression(property, ExpressionType.greaterOrEqual, new Object[]{value.getTime()}));
        return (T)this;
    }

    //**********************************************************************************************

    public T greaterOrEqual(ChildQuery leftChildQuery, ChildQuery rightChildQuery) {
        applyCondition.add(new Expression(leftChildQuery.getSql(), ExpressionType.greaterOrEqual, rightChildQuery.getSql()));
        return (T)this;
    }

    public T greaterOrEqual(Property property, ChildQuery childQuery) {
        applyCondition.add(new Expression(property, ExpressionType.greaterOrEqual, childQuery.getSql()));
        return (T)this;
    }

    public T greaterOrEqual(ChildQuery childQuery, long value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.greaterOrEqual, new Object[]{value}));
        return (T)this;
    }

    public T greaterOrEqual(ChildQuery childQuery, double value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.greaterOrEqual, new Object[]{value}));
        return (T)this;
    }

    public T greaterOrEqual(ChildQuery childQuery, byte[] value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.greaterOrEqual, new Object[]{value}));
        return (T)this;
    }

    public T greaterOrEqual(ChildQuery childQuery, String value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.greaterOrEqual, new Object[]{value}));
        return (T)this;
    }

    public T greaterOrEqual(ChildQuery childQuery, Date value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.greaterOrEqual, new Object[]{value.getTime()}));
        return (T)this;
    }

    //-----------------------------------between(between...and...)----------------------------------

    public T between(Property property, long value1, long value2) {
        applyCondition.add(new Expression(property, ExpressionType.between, new Object[]{value1, value2}));
        return (T)this;
    }

    public T between(Property property, double value1, double value2) {
        applyCondition.add(new Expression(property, ExpressionType.between, new Object[]{value1, value2}));
        return (T)this;
    }

    public T between(Property property, Date value1, Date value2) {
        applyCondition.add(new Expression(property, ExpressionType.between, new Object[]{value1.getTime(), value2.getTime()}));
        return (T)this;
    }

    //**********************************************************************************************

    public T between(ChildQuery childQuery, long value1, long value2) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.between, new Object[]{value1, value2}));
        return (T)this;
    }

    public T between(ChildQuery childQuery, double value1, double value2) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.between, new Object[]{value1, value2}));
        return (T)this;
    }

    public T between(ChildQuery childQuery, Date value1, Date value2) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.between, new Object[]{value1.getTime(), value2.getTime()}));
        return (T)this;
    }

    //------------------------------------in(in(?,?,?))---------------------------------------------
    public T in(Property property, Integer... value) {
        applyCondition.add(new Expression(property, ExpressionType.in, Arrays.asList(value).toArray()));
        return (T)this;
    }

    public T in(Property property, Long... value) {
        applyCondition.add(new Expression(property, ExpressionType.in, Arrays.asList(value).toArray()));
        return (T)this;
    }

    public T in(Property property, String... value) {
        applyCondition.add(new Expression(property, ExpressionType.in, Arrays.asList(value).toArray()));
        return (T)this;
    }

    public T in(Property property, Collection<?> value) {
        applyCondition.add(new Expression(property, ExpressionType.in, value.toArray()));
        return (T)this;
    }

    //**********************************************************************************************

    public T in(ChildQuery leftChildQuery, ChildQuery rightChildQuery) {
        applyCondition.add(new Expression(leftChildQuery.getSql(), ExpressionType.in, rightChildQuery.getSql()));
        return (T)this;
    }

    public T in(Property property, ChildQuery childQuery) {
        applyCondition.add(new Expression(property, ExpressionType.in, childQuery.getSql()));
        return (T)this;
    }

    public T in(ChildQuery childQuery, Integer... value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.in, Arrays.asList(value).toArray()));
        return (T)this;
    }

    public T in(ChildQuery childQuery, Long... value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.in, Arrays.asList(value).toArray()));
        return (T)this;
    }

    public T in(ChildQuery childQuery, String... value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.in, Arrays.asList(value).toArray()));
        return (T)this;
    }

    public T in(ChildQuery childQuery, Collection<?> value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.in, value.toArray()));
        return (T)this;
    }

    //--------------------------------not in(not in(?,?,?))-----------------------------------------

    public T notIn(Property property, Integer... value) {
        applyCondition.add(new Expression(property, ExpressionType.notIn, Arrays.asList(value).toArray()));
        return (T)this;
    }

    public T notIn(Property property, Long... value) {
        applyCondition.add(new Expression(property, ExpressionType.notIn, Arrays.asList(value).toArray()));
        return (T)this;
    }

    public T notIn(Property property, String... value) {
        applyCondition.add(new Expression(property, ExpressionType.notIn, Arrays.asList(value).toArray()));
        return (T)this;
    }

    public T notIn(Property property, Collection<?> value) {
        applyCondition.add(new Expression(property, ExpressionType.notIn, value.toArray()));
        return (T)this;
    }

    //**********************************************************************************************

    public T notIn(ChildQuery leftChildQuery, ChildQuery rightChildQuery) {
        applyCondition.add(new Expression(leftChildQuery.getSql(), ExpressionType.notIn, rightChildQuery.getSql()));
        return (T)this;
    }

    public T notIn(Property property, ChildQuery childQuery) {
        applyCondition.add(new Expression(property, ExpressionType.notIn, childQuery.getSql()));
        return (T)this;
    }

    public T notIn(ChildQuery childQuery, Integer... value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.notIn, Arrays.asList(value).toArray()));
        return (T)this;
    }

    public T notIn(ChildQuery childQuery, Long... value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.notIn, Arrays.asList(value).toArray()));
        return (T)this;
    }

    public T notIn(ChildQuery childQuery, String... value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.notIn, Arrays.asList(value).toArray()));
        return (T)this;
    }

    public T notIn(ChildQuery childQuery, Collection<?> value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.notIn, value.toArray()));
        return (T)this;
    }

    //------------------------------------like(like 自定义表达式规则匹配)-------------------------------

    public T like(Property property, String exp) {
        applyCondition.add(new Expression(property, ExpressionType.like, new Object[]{exp}));
        return (T)this;
    }

    //**********************************************************************************************

    public T like(ChildQuery childQuery, String exp) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.like, new Object[]{exp}));
        return (T)this;
    }

    //------------------------------------contains(contains '%?%')-----------------------------------------

    public T contains(Property property, String value) {
        applyCondition.add(new Expression(property, ExpressionType.contains, new Object[]{value}));
        return (T)this;
    }

    //**********************************************************************************************

    public T contains(ChildQuery childQuery, String value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.contains, new Object[]{value}));
        return (T)this;
    }

    //------------------------------------startWith(like '?%')-----------------------------------------

    public T startWith(Property property, String value) {
        applyCondition.add(new Expression(property, ExpressionType.startWith, new Object[]{value}));
        return (T)this;
    }

    //**********************************************************************************************

    public T startWith(ChildQuery childQuery, String value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.startWith, new Object[]{value}));
        return (T)this;
    }

    //------------------------------------endWith(like '%?')-----------------------------------------

    public T endWith(Property property, String value) {
        applyCondition.add(new Expression(property, ExpressionType.endWith, new Object[]{value}));
        return (T)this;
    }

    //**********************************************************************************************

    public T endWith(ChildQuery childQuery, String value) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.endWith, new Object[]{value}));
        return (T)this;
    }

    //------------------------------------regExp(regExp '^a.*')-------------------------------------

    public T regExp(Property property, String regExp) {
        applyCondition.add(new Expression(property, ExpressionType.regExp, new Object[]{regExp}));
        return (T)this;
    }

    //**********************************************************************************************

    public T regExp(ChildQuery childQuery, String regExp) {
        applyCondition.add(new Expression(childQuery.getSql(), ExpressionType.regExp, new Object[]{regExp}));
        return (T)this;
    }

}
