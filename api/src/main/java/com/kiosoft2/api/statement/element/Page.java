package com.kiosoft2.api.statement.element;

public class Page {

    /**每页条数*/
    public int limit;

    /**偏移量*/
    public int offset;

    public Page() {
    }

    public Page(int limit, int offset) {
        this.limit = limit;
        this.offset = offset;
    }
}
