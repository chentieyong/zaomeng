package com.kingpivot.protocol;

import com.kingpivot.common.utils.TPage;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/9/12.
 */
public class MessagePage<T> implements Serializable {

    //总记录数
    private int total = 0;
    //当前页码
    private int currentPage = 0;

    //当前页记录数
    private int currentPgeNumber = 0;

    //每页多少记录
    private int pageNumber = 0;
    //总页数
    private int totalPage = 0;
    //是否有下一页
    private Boolean hasNextPage;
    //当前页数据
    private List<T> rows;

    public MessagePage(TPage page, List<T> rows) {
        this.total = page.getTotalSize();
        this.currentPage = page.getCurrentPage();
        this.pageNumber = page.getPageSize();
        if (null != rows) {
            this.currentPgeNumber = rows.size();
        }
        this.rows = rows;
        if (pageNumber != 0) {

        }
        this.totalPage = (this.total + this.pageNumber - 1) / this.pageNumber;
        if (this.currentPage < this.totalPage) {
            this.hasNextPage = true;
        } else {
            this.hasNextPage = false;
        }
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }


    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public Boolean getHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(Boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public int getCurrentPgeNumber() {
        return currentPgeNumber;
    }

    public void setCurrentPgeNumber(int currentPgeNumber) {
        this.currentPgeNumber = currentPgeNumber;
    }
}
