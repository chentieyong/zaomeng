package com.kingpivot.common.utils;

/**
 * Created by Administrator on 2015/9/17.
 */
public class ApiPageUtil {
    /**
     * 构造分页
     * */
    public static TPage makePage(Object currentPageOb, Object pageNumberOb) {
        Integer currentPage = 1;
        Integer pageNumber = 10;
        if (null != currentPageOb) {
            currentPage = Integer.valueOf((String) currentPageOb);
        }
        if (null != pageNumberOb) {
            pageNumber = Integer.valueOf((String) pageNumberOb);
        }
        //构造分页条件对象
        TPage page = new TPage(currentPage, pageNumber);
        page.setCurrentPage(currentPage);
        page.setPageSize(pageNumber);
        return page;
    }
}
