package com.kiosoft2.api.builder;

import androidx.room.RoomDatabase;

import com.kiosoft2.api.Entity;
import com.kiosoft2.api.Property;
import com.kiosoft2.api.statement.element.Condition;
import com.kiosoft2.api.statement.element.Expression;
import com.kiosoft2.api.statement.element.Join;
import com.kiosoft2.api.type.ConditionRelation;
import com.kiosoft2.api.type.ExpressionType;
import com.kiosoft2.api.type.JoinType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 
 * @createDate: 2023/8/15 15:30
 * @version: 1.0
 * @description: 联合查询builder
 */
public class JoinBuilder<T> extends BasicConditionRelationBuilder<T,JoinBuilder<T>> {

    private final RoomDatabase db;
    private final Entity<T> entity;
    private final List<Join> joinList = new ArrayList<>();
    private Join join;


    public JoinBuilder(JoinType joinType, RoomDatabase db, Entity<T> entity, Entity<?> rightEntity) {
        this.db = db;
        this.entity = entity;

        this.join = new Join(joinType,rightEntity,super.condition);
        this.joinList.add(join);
    }

    //------------------------------------------join------------------------------------------------

    public JoinBuilder<T> innerJoin(Entity<?> targetEntity){
        this.applyWhere(new Condition(ConditionRelation.and, new ArrayList<>(), new ArrayList<>()));
        this.join = new Join(JoinType.innerJoin,targetEntity,this.getApplyWhere());
        this.joinList.add(join);
        return this;
    }

    public JoinBuilder<T> leftJoin(Entity<?> targetEntity){
        this.applyWhere(new Condition(ConditionRelation.and, new ArrayList<>(), new ArrayList<>()));
        this.join = new Join(JoinType.leftOuterJoin,targetEntity,this.getApplyWhere());
        this.joinList.add(join);
        return this;
    }

    public JoinBuilder<T> crossJoin(Entity<?> targetEntity){
        this.applyWhere(new Condition(ConditionRelation.and, new ArrayList<>(), new ArrayList<>()));
        this.join = new Join(JoinType.crossJoin,targetEntity,this.getApplyWhere());
        this.joinList.add(join);
        return this;
    }

    //-----------------------------------------query------------------------------------------------

    /**
     * 查询builder
     */
    public QueryBuilder<T> query() {
        return new QueryBuilder<T>(db, entity,null,joinList);
    }

    /**
     * 查询builder
     * @param columns 查询字段列表
     */
    public QueryBuilder<T> query(Property... columns){
        return new QueryBuilder<>(db,entity, Arrays.asList(columns),joinList);
    }

    /**
     * 查询builder
     * @param columns 查询字段列表
     */
    public QueryBuilder<T> query(List<Property> columns){
        return new QueryBuilder<>(db,entity,columns,joinList);
    }

    //-----------------------------------------condition--------------------------------------------
    public JoinBuilder<T> equal(Property property1, Property property2) {
        getApplyWhere().add(Expression.newValueProperty(property1, ExpressionType.equal,new Object[]{property2}));
        return this;
    }

    public JoinBuilder<T> notEqual(Property property1, Property property2) {
        getApplyWhere().add(Expression.newValueProperty(property1, ExpressionType.notEqual,new Object[]{property2}));
        return this;
    }

    public  JoinBuilder<T> less(Property property1, Property property2) {
        getApplyWhere().add(Expression.newValueProperty(property1, ExpressionType.less,new Object[]{property2}));
        return this;
    }

    public  JoinBuilder<T> lessOrEqual(Property property1, Property property2) {
        getApplyWhere().add(Expression.newValueProperty(property1, ExpressionType.lessOrEqual,new Object[]{property2}));
        return this;
    }

    public  JoinBuilder<T> greater(Property property1, Property property2) {
        getApplyWhere().add(Expression.newValueProperty(property1, ExpressionType.greater,new Object[]{property2}));
        return this;
    }

    public  JoinBuilder<T> greaterOrEqual(Property property1, Property property2) {
        getApplyWhere().add(Expression.newValueProperty(property1, ExpressionType.greaterOrEqual,new Object[]{property2}));
        return this;
    }

    public  JoinBuilder<T> between(Property property1, Property property2) {
        getApplyWhere().add(Expression.newValueProperty(property1, ExpressionType.between,new Object[]{property2}));
        return this;
    }

    public  JoinBuilder<T> in(Property property, Property... values) {
        getApplyWhere().add(Expression.newValueProperty(property, ExpressionType.in,values));
        return this;
    }

    @Override
    public JoinBuilder<T> in(Property property, Collection<?> value) {
        getApplyWhere().add(Expression.newValueProperty(property, ExpressionType.in,value.toArray()));
        return this;
    }

    public  JoinBuilder<T> notIn(Property property, Property... values) {
        getApplyWhere().add(Expression.newValueProperty(property, ExpressionType.notIn,values));
        return this;
    }

    @Override
    public JoinBuilder<T> notIn(Property property, Collection<?> value) {
        getApplyWhere().add(Expression.newValueProperty(property, ExpressionType.notIn,value.toArray()));
        return this;
    }

    public  JoinBuilder<T> like(Property property1, Property property2) {
        getApplyWhere().add(Expression.newValueProperty(property1, ExpressionType.like,new Object[]{property2}));
        return this;
    }

    public  JoinBuilder<T> contains(Property property1, Property property2) {
        getApplyWhere().add(Expression.newValueProperty(property1, ExpressionType.contains,new Object[]{property2}));
        return this;
    }

    public  JoinBuilder<T> startWith(Property property1, Property property2) {
        getApplyWhere().add(Expression.newValueProperty(property1, ExpressionType.startWith,new Object[]{property2}));
        return this;
    }

    public  JoinBuilder<T> endWith(Property property1, Property property2) {
        getApplyWhere().add(Expression.newValueProperty(property1, ExpressionType.endWith,new Object[]{property2}));
        return this;
    }

    public  JoinBuilder<T> regExp(Property property1, Property property2) {
        getApplyWhere().add(Expression.newValueProperty(property1, ExpressionType.regExp,new Object[]{property2}));
        return this;
    }
}
