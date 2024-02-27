package com.kiosoft2.api;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.kiosoft2.api.builder.ChildQuery;
import com.kiosoft2.api.condition.PropertyCondition;
import com.kiosoft2.api.type.ExpressionType;
import com.kiosoft2.api.type.FuncType;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

/**
 * @createDate: 2023/8/7 14:17
 * @version: 1.0
 * @description: Entity 属性信息
 * timeFormat:
 * %d	一月中的第几天，01-31
 * %f	带小数部分的秒，SS.SSS
 * %H	小时，00-23
 * %j	一年中的第几天，001-366
 * %J	儒略日数，DDDD.DDDD
 * %m	月，00-12
 * %M	分，00-59
 * %s	从 1970-01-01 算起的秒数
 * %S	秒，00-59
 * %w	一周中的第几天，0-6 (0 is Sunday)
 * %W	一年中的第几周，01-53
 * %Y	年，YYYY
 * %%	% symbol
 */
public class Property implements Serializable {

    /**该属性所属表名*/
    public final String tableName;

    /**在java中的列名*/
    public final String javaName;

    /**数据表字段属性名称*/
    public final String name;

    /**属性类型*/
    public final Class<?> type;

    /**是否属于主键Id*/
    public final boolean isId;

    /**该属性设置的默认值*/
    public final String defaultValue;

    /**该Property对象是否属于复制对象，为了解决多次调用例如alias、distinct的方法时所重复产生的新对象的问题*/
    private final boolean isClone;

    /**数据表字段别名*/
    private String alias;

    /**该字段属性选择使用的sqlite函数*/
    private FuncType funcType;

    /**自定义时间转换格式(字段值为时间戳或时间格式串时有效)*/
    private String timeFormat;

    public Property(String tableName, String javaName, String name, Class<?> type,
                    boolean isId, String defaultValue) {
        this(tableName,javaName,name,type,isId,defaultValue,false);
    }

    private Property(String tableName, String javaName, String name, Class<?> type,
                     boolean isId, String defaultValue,boolean isClone) {
        this.tableName = tableName;
        this.javaName = javaName;
        this.name = name;
        this.type = type;
        this.isId = isId;
        this.defaultValue = defaultValue;
        this.isClone = isClone;
    }

    /**
     * 更新并获取Property对象
     * @param alias 字段别名
     * @param funcType 指定使用的函数
     */
    private Property updateGet(String alias,FuncType funcType){
        if(!isClone){
            Property p = new Property(
                    tableName
                    , javaName
                    , name
                    , type
                    , isId
                    , defaultValue
                    , true
            );
            p.alias = alias;
            p.funcType = funcType;
            return p;
        }

        if(!TextUtils.isEmpty(alias)){
            if(!TextUtils.isEmpty(this.alias)){
                throw new IllegalArgumentException(String.format("%s字段属性已存在别名%s",name,this.alias));
            }
            this.alias = alias;
        }

        if(funcType != null){
            if(this.funcType != null){
                throw new IllegalArgumentException(String.format("%s字段属性已存在指定函数%s",name,this.funcType.name()));
            }
            this.funcType = funcType;
        }

        return this;
    }

    public String getAlias() {
        return alias;
    }

    public FuncType getFuncType() {
        return funcType;
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public Property alias(String alias){
        return this.updateGet(alias,null);
    }

    public Property count(){
        return this.updateGet(null,FuncType.count);
    }

    public Property max(){
        return this.updateGet(null,FuncType.max);
    }

    public Property min(){
        return this.updateGet(null,FuncType.min);
    }

    public Property avg(){
        return this.updateGet(null,FuncType.avg);
    }

    public Property sum(){
        return this.updateGet(null,FuncType.sum);
    }

    public Property abs(){
        return this.updateGet(null,FuncType.abs);
    }

    public Property upper(){
        return this.updateGet(null,FuncType.upper);
    }

    public Property lower(){
        return this.updateGet(null,FuncType.lower);
    }

    public Property length(){
        return this.updateGet(null,FuncType.length);
    }

    public Property formatTime(String format){
        this.timeFormat = format;
        return this.updateGet(null,FuncType.strFTime);
    }

    public Property dateTime(){
        return this.updateGet(null,FuncType.dateTime);
    }

    public Property date(){
        return this.updateGet(null,FuncType.date);
    }

    public Property time(){
        return this.updateGet(null,FuncType.time);
    }

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }

    //--------------------------------------isNull and notNull--------------------------------------

    public PropertyCondition isNull() {
        return new PropertyCondition(this, ExpressionType.isNull);
    }

    public PropertyCondition notNull() {
        return new PropertyCondition(this, ExpressionType.notNull);
    }

    //----------------------------------------equal(=)-------------------------------------------------

    public PropertyCondition equal(long value) {
        return new PropertyCondition(this, ExpressionType.equal, new Object[]{value});
    }

    public PropertyCondition equal(double value) {
        return new PropertyCondition(this, ExpressionType.equal, new Object[]{value});
    }

    public PropertyCondition equal(byte[] value) {
        return new PropertyCondition(this, ExpressionType.equal, new Object[]{value});
    }

    public PropertyCondition equal(String value) {
        return new PropertyCondition(this, ExpressionType.equal, new Object[]{value});
    }

    public PropertyCondition equal(boolean value) {
        return new PropertyCondition(this, ExpressionType.equal, new Object[]{value});
    }

    public PropertyCondition equal(Date value) {
        return new PropertyCondition(this, ExpressionType.equal, new Object[]{value.getTime()});
    }

    public PropertyCondition equal(ChildQuery childQuery) {
        return new PropertyCondition(this, ExpressionType.equal, childQuery);
    }

    //---------------------------------------notEqual(!=)-----------------------------------------------

    public PropertyCondition notEqual(long value) {
        return new PropertyCondition(this, ExpressionType.notEqual, new Object[]{value});
    }

    public PropertyCondition notEqual(double value) {
        return new PropertyCondition(this, ExpressionType.notEqual, new Object[]{value});
    }

    public PropertyCondition notEqual(byte[] value) {
        return new PropertyCondition(this, ExpressionType.notEqual, new Object[]{value});
    }

    public PropertyCondition notEqual(String value) {
        return new PropertyCondition(this, ExpressionType.notEqual, new Object[]{value});
    }

    public PropertyCondition notEqual(boolean value) {
        return new PropertyCondition(this, ExpressionType.notEqual, new Object[]{value});
    }

    public PropertyCondition notEqual(Date value) {
        return new PropertyCondition(this, ExpressionType.notEqual, new Object[]{value.getTime()});
    }

    public PropertyCondition notEqual(ChildQuery childQuery) {
        return new PropertyCondition(this, ExpressionType.notEqual, childQuery);
    }

    //-----------------------------------------less(<)----------------------------------------------
    public PropertyCondition less(long value) {
        return new PropertyCondition(this, ExpressionType.less, new Object[]{value});
    }

    public PropertyCondition less(double value) {
        return new PropertyCondition(this, ExpressionType.less, new Object[]{value});
    }

    public PropertyCondition less(byte[] value) {
        return new PropertyCondition(this, ExpressionType.less, new Object[]{value});
    }

    public PropertyCondition less(String value) {
        return new PropertyCondition(this, ExpressionType.less, new Object[]{value});
    }

    public PropertyCondition less(Date value) {
        return new PropertyCondition(this, ExpressionType.less, new Object[]{value.getTime()});
    }

    public PropertyCondition less(ChildQuery childQuery) {
        return new PropertyCondition(this, ExpressionType.less, childQuery);
    }

    //----------------------------------------lessOrEqual(<=)-------------------------------------------------

    public PropertyCondition lessOrEqual(long value) {
        return new PropertyCondition(this, ExpressionType.lessOrEqual, new Object[]{value});
    }

    public PropertyCondition lessOrEqual(double value) {
        return new PropertyCondition(this, ExpressionType.lessOrEqual, new Object[]{value});
    }

    public PropertyCondition lessOrEqual(byte[] value) {
        return new PropertyCondition(this, ExpressionType.lessOrEqual, new Object[]{value});
    }

    public PropertyCondition lessOrEqual(String value) {
        return new PropertyCondition(this, ExpressionType.lessOrEqual, new Object[]{value});
    }

    public PropertyCondition lessOrEqual(Date value) {
        return new PropertyCondition(this, ExpressionType.lessOrEqual, new Object[]{value.getTime()});
    }

    public PropertyCondition lessOrEqual(ChildQuery childQuery) {
        return new PropertyCondition(this, ExpressionType.lessOrEqual, childQuery);
    }

    //----------------------------------------greater(>)-------------------------------------------------

    public PropertyCondition greater(long value) {
        return new PropertyCondition(this, ExpressionType.greater, new Object[]{value});
    }

    public PropertyCondition greater(double value) {
        return new PropertyCondition(this, ExpressionType.greater, new Object[]{value});
    }

    public PropertyCondition greater(byte[] value) {
        return new PropertyCondition(this, ExpressionType.greater, new Object[]{value});
    }

    public PropertyCondition greater(String value) {
        return new PropertyCondition(this, ExpressionType.greater, new Object[]{value});
    }

    public PropertyCondition greater(Date value) {
        return new PropertyCondition(this, ExpressionType.greater, new Object[]{value.getTime()});
    }

    public PropertyCondition greater(ChildQuery childQuery) {
        return new PropertyCondition(this, ExpressionType.greater, childQuery);
    }

    //-------------------------------greaterOrEqual(>=)---------------------------------------------

    public PropertyCondition greaterOrEqual(long value) {
        return new PropertyCondition(this, ExpressionType.greaterOrEqual, new Object[]{value});
    }

    public PropertyCondition greaterOrEqual(double value) {
        return new PropertyCondition(this, ExpressionType.greaterOrEqual, new Object[]{value});
    }

    public PropertyCondition greaterOrEqual(byte[] value) {
        return new PropertyCondition(this, ExpressionType.greaterOrEqual, new Object[]{value});
    }

    public PropertyCondition greaterOrEqual(String value) {
        return new PropertyCondition(this, ExpressionType.greaterOrEqual, new Object[]{value});
    }

    public PropertyCondition greaterOrEqual(Date value) {
        return new PropertyCondition(this, ExpressionType.greaterOrEqual, new Object[]{value.getTime()});
    }

    public PropertyCondition greaterOrEqual(ChildQuery childQuery) {
        return new PropertyCondition(this, ExpressionType.greaterOrEqual, childQuery);
    }

    //-----------------------------------between(between...and...)----------------------------------

    public PropertyCondition between(long value1, long value2) {
        return new PropertyCondition(this, ExpressionType.between, new Object[]{value1, value2});
    }

    public PropertyCondition between(double value1, double value2) {
        return new PropertyCondition(this, ExpressionType.between, new Object[]{value1, value2});
    }

    public PropertyCondition between(Date value1, Date value2) {
        return new PropertyCondition(this, ExpressionType.between, new Object[]{value1.getTime(), value2.getTime()});
    }

    //------------------------------------in(in(?,?,?))---------------------------------------------
    public PropertyCondition in(Integer... value) {
        return new PropertyCondition(this, ExpressionType.in, Arrays.asList(value).toArray());
    }

    public PropertyCondition in(Long... value) {
        return new PropertyCondition(this, ExpressionType.in, Arrays.asList(value).toArray());
    }

    public PropertyCondition in(String... value) {
        return new PropertyCondition(this, ExpressionType.in, Arrays.asList(value).toArray());
    }

    public PropertyCondition in(Collection<?> value) {
        return new PropertyCondition(this, ExpressionType.in, value.toArray());
    }

    public PropertyCondition in(ChildQuery childQuery) {
        return new PropertyCondition(this, ExpressionType.in, childQuery);
    }

    //--------------------------------not in(not in(?,?,?))-----------------------------------------

    public PropertyCondition notIn(Integer... value) {
        return new PropertyCondition(this, ExpressionType.notIn, Arrays.asList(value).toArray());
    }

    public PropertyCondition notIn(Long... value) {
        return new PropertyCondition(this, ExpressionType.notIn, Arrays.asList(value).toArray());
    }

    public PropertyCondition notIn(String... value) {
        return new PropertyCondition(this, ExpressionType.notIn, Arrays.asList(value).toArray());
    }

    public PropertyCondition notIn(Collection<?> value) {
        return new PropertyCondition(this, ExpressionType.notIn, value.toArray());
    }

    public PropertyCondition notIn(ChildQuery childQuery) {
        return new PropertyCondition(this, ExpressionType.notIn, childQuery);
    }

    //------------------------------------like(like 自定义表达式规则匹配)-----------------------------------------

    public PropertyCondition like(String exp) {
        return new PropertyCondition(this, ExpressionType.like, new Object[]{exp});
    }

    public PropertyCondition like(ChildQuery childQuery) {
        return new PropertyCondition(this, ExpressionType.like, childQuery);
    }

    //------------------------------------contains(contains '%?%')-----------------------------------------

    public PropertyCondition contains(String value) {
        return new PropertyCondition(this, ExpressionType.contains, new Object[]{value});
    }

    public PropertyCondition contains(ChildQuery childQuery) {
        return new PropertyCondition(this, ExpressionType.contains, childQuery);
    }

    //------------------------------------startWith(like '?%')-----------------------------------------

    public PropertyCondition startWith(String value) {
        return new PropertyCondition(this, ExpressionType.startWith, new Object[]{value});
    }

    public PropertyCondition startWith(ChildQuery childQuery) {
        return new PropertyCondition(this, ExpressionType.startWith, childQuery);
    }

    //------------------------------------endWith(like '%?')-----------------------------------------

    public PropertyCondition endWith(String value) {
        return new PropertyCondition(this, ExpressionType.endWith, new Object[]{value});
    }

    public PropertyCondition endWith(ChildQuery childQuery) {
        return new PropertyCondition(this, ExpressionType.endWith, childQuery);
    }

    //------------------------------------regExp(regExp '^a.*')-----------------------------------------

    public PropertyCondition regExp(String regExp) {
        return new PropertyCondition(this, ExpressionType.regExp, new Object[]{regExp});
    }

    public PropertyCondition regExp(ChildQuery childQuery) {
        return new PropertyCondition(this, ExpressionType.regExp, childQuery);
    }

}
