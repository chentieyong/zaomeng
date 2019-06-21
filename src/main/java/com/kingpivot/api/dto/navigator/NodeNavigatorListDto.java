package com.kingpivot.api.dto.navigator;

import com.kingpivot.api.dto.release.ReleaseArticleListDto;
import com.kingpivot.api.dto.release.ReleaseGoodsShopListDto;

import java.util.List;

public class NodeNavigatorListDto {
    private String id;
    private String name;
    private String description;
    private String largeIcon = "";
    private Long orderSeq;
    private String parentID;
    private String samllIcon = "";
    private String functionUrl;
    private List<ReleaseGoodsShopListDto> goodsList;
    private List<ReleaseArticleListDto> articleList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLargeIcon() {
        return largeIcon;
    }

    public void setLargeIcon(String largeIcon) {
        this.largeIcon = largeIcon;
    }

    public Long getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(Long orderSeq) {
        this.orderSeq = orderSeq;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getSamllIcon() {
        return samllIcon;
    }

    public void setSamllIcon(String samllIcon) {
        this.samllIcon = samllIcon;
    }

    public String getFunctionUrl() {
        return functionUrl;
    }

    public void setFunctionUrl(String functionUrl) {
        this.functionUrl = functionUrl;
    }

    public List<ReleaseGoodsShopListDto> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<ReleaseGoodsShopListDto> goodsList) {
        this.goodsList = goodsList;
    }

    public List<ReleaseArticleListDto> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<ReleaseArticleListDto> articleList) {
        this.articleList = articleList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
