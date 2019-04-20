package com.kingpivot.common.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class TPage implements Serializable {

    private static final long serialVersionUID = 4836912765553524379L;

    public final static int PAGESIZE_DEFAULT = 10;

    public final static int CURRENTPAGE_DEFAULT = 1; //从1开始

    private int pageSize = PAGESIZE_DEFAULT; // 每页条数  <=0 表示不分页

    private int currentPage = CURRENTPAGE_DEFAULT;

    private int totalSize; //总条数

    public static final TPage _DEFAULT = new TPage(0, PAGESIZE_DEFAULT);

    public TPage() {

    }


    public TPage(int currentPage, int pageSize) {
        this.setPageSize(pageSize);
        this.setCurrentPage(currentPage);
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(int currentPage) {
        if (currentPage < 1) {
            currentPage = CURRENTPAGE_DEFAULT;
        }
        this.currentPage = currentPage;
    }

    public int getTotalSize() {
        return this.totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public static int getPagesizeDefault() {
        return PAGESIZE_DEFAULT;
    }

    @JsonIgnore
    public int getStart() {
        return (this.currentPage - 1) * this.pageSize;
    }

    @JsonIgnore
    public int getLimit() {
        return this.pageSize;
    }

    public TPage nextPage() {
        this.currentPage += 1;
        return this;
    }

    public TPage perviousPage() {
        if (this.currentPage > CURRENTPAGE_DEFAULT) {
            this.currentPage -= 1;
        }
        return this;
    }

    //总共多少页
    public int getTotalPage() {
        if (pageSize <= 0)
            return 1;
        return (this.totalSize + this.pageSize - 1) / this.pageSize;
    }

    @Override
    public String toString() {
        return String.format(
                "Page [pageSize=%s, currentPage=%s, totalSize=%s]", pageSize,
                currentPage, totalSize);
    }


}
