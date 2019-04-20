package com.kingpivot.common.util;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/10/19.
 */
public class TreeInfoDTO<T> implements Serializable{

    private Integer total = 0;

    private Integer levelTotal = 0;

    private T data;

    public TreeInfoDTO() {
    }

    public TreeInfoDTO(Integer total, Integer levelTotal, T data) {
        this.total = total;
        this.levelTotal = levelTotal;
        this.data = data;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getLevelTotal() {
        return levelTotal;
    }

    public void setLevelTotal(Integer levelTotal) {
        this.levelTotal = levelTotal;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
