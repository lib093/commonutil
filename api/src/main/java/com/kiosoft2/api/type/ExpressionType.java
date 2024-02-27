package com.kiosoft2.api.type;

public enum ExpressionType {

    equal("= ?","= %s"),//等于
    notEqual("!= ?","!= %s"),//不等于
    less("< ?","< %s"),//小于
    lessOrEqual("<= ?","<= %s"),//小于等于
    greater("> ?", "> %s"),//大于
    greaterOrEqual(">= ?",">= %s"),//大于等于
    between("between ? and ?","between %s and %s"),//两者之间
    in("in","in(%s)"),//包含
    notIn("not in","not in(%s)"),//不包含
    like("like ?","like %s"),//自定义匹配规则匹配
    contains("like '%' || ? || '%'","like '%' || %s || '%'"),//完全模糊匹配
    startWith("like ? || '%'","like %s || '%'"),//以什么开头
    endWith("like '%' || ?","like '%' || %s"),//以什么结尾
    regExp("regexp ?","regexp %s"),//检查一个字符串是否与指定的正则表达式模式匹配
    isNull("is null","is null"),//为null
    notNull("is not null","is not null"),//不为null
    exists("exists","exists"),//判断记录是否存在
    notExists("not exists","not exists"),//判断记录是否不存在
    ;

    /**对应sqlite表达式*/
    private final String exp;

    /**格式化占位方式表达式字符串*/
    private final String format;

    ExpressionType(String exp, String format){
        this.exp = exp;
        this.format = format;
    }

    public String getExp() {
        return exp;
    }

    public String getFormat() {
        return format;
    }
}
