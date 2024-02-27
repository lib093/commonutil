package com.kiosoft2.processor;

/**
 * @version: 1.0
 * @description: 实体关联信息
 */
public class EntityRelation {

    public final String entityClass;

    public final String relationEntityClass;

    public EntityRelation(String entityClass, String relationEntityClass) {
        this.entityClass = entityClass;
        this.relationEntityClass = relationEntityClass;
    }
}
