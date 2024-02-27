package com.kiosoft2.api.type;

public enum JoinType {

    /**内连接, 它用于组合满足连接条件的多个表中的所有行记录*/
    innerJoin("inner join"),

    /**左外连接,用于从ON条件中指定的左侧表中获取所有行，并且仅右表中满足连接条件的那些行记录*/
    leftOuterJoin("left outer join"),

    /**交叉连接,用于将第一个表的每一行与第二个表的每一行进行匹配。 如果第一个表包含x列，而第二个表包含y列，则所得到的交叉连接表的结果将包含x * y列。*/
    crossJoin("cross join"),
    ;

    /**对应sqlite表达式*/
    private final String exp;

    JoinType(String exp){
        this.exp = exp;
    }

    public String getExp() {
        return exp;
    }

}
