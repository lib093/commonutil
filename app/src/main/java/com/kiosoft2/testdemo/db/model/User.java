package com.kiosoft2.testdemo.db.model;

import androidx.annotation.Keep;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "tb_user")
public class User implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "user_name")
    private String name;

    @ColumnInfo(defaultValue = "10")
    private int age;
    private float a1;
    private long a2;
    private byte a3;
    private byte[] a4;
    private short a5;
    private Float a6;
    private Long a7;
    private Byte a8;
    private Short a9;
    private Integer a10;
    private Boolean a11;

    private String address;

    private boolean sex;

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getA1() {
        return a1;
    }

    public void setA1(float a1) {
        this.a1 = a1;
    }

    public long getA2() {
        return a2;
    }

    public void setA2(long a2) {
        this.a2 = a2;
    }

    public byte getA3() {
        return a3;
    }

    public void setA3(byte a3) {
        this.a3 = a3;
    }

    public byte[] getA4() {
        return a4;
    }

    public void setA4(byte[] a4) {
        this.a4 = a4;
    }

    public short getA5() {
        return a5;
    }

    public void setA5(short a5) {
        this.a5 = a5;
    }

    public Float getA6() {
        return a6;
    }

    public void setA6(Float a6) {
        this.a6 = a6;
    }

    public Long getA7() {
        return a7;
    }

    public void setA7(Long a7) {
        this.a7 = a7;
    }

    public Byte getA8() {
        return a8;
    }

    public void setA8(Byte a8) {
        this.a8 = a8;
    }

    public Short getA9() {
        return a9;
    }

    public void setA9(Short a9) {
        this.a9 = a9;
    }

    public Integer getA10() {
        return a10;
    }

    public void setA10(Integer a10) {
        this.a10 = a10;
    }

    public Boolean isA11() {
        return a11;
    }

    public void setA11(Boolean a11) {
        this.a11 = a11;
    }
}
