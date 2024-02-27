package com.kiosoft2.api.statement.element;

import com.kiosoft2.api.Entity;
import com.kiosoft2.api.type.JoinType;

public class Join {

    /**join类型*/
    public final JoinType type;

    /**join目标表实体信息*/
    public final Entity<?> targetEntity;

    /**on条件*/
    public final Condition on;

    public Join(JoinType type, Entity<?> targetEntity, Condition on) {
        this.type = type;
        this.targetEntity = targetEntity;
        this.on = on;
    }
}
