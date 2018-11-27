package com.icetech.common.domain.response;

import lombok.ToString;

import java.io.Serializable;

@ToString
public class PageQuery<T> implements Serializable {
    //默认页码
    private static final int DEFAULT_CURRENT = 1;
    //默认每页条数
    private static final int DEFAULT_SIZE = 15;

    public PageQuery(){
        this.firstResult= (DEFAULT_CURRENT - 1) * DEFAULT_SIZE;
        this.size = DEFAULT_SIZE;
    }
    public PageQuery(int current, int size){
        this.firstResult= (current - 1) * size;
        this.size = size;
    }
    private T param;

    /**
     * Limit 后的第一个参数
     */
    private int current;
    /**
     * Limit 后的第一个参数
     */
    private int firstResult;
    /**
     * 每页条数
     */
    private int size;

    public int getSize() {
        return size;
    }

    public T getParam() {
        return param;
    }

    public void setParam(T param) {
        this.param = param;
    }

    public int getCurrent() {
        return current;
    }

}
