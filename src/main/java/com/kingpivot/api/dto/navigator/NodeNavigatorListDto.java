package com.kingpivot.api.dto.navigator;

import com.kingpivot.api.dto.release.ReleaseGoodsShopListDto;

import java.util.List;

public class NodeNavigatorListDto {
    private String id;
    private String name;
    private String largeIcon = "";
    private Long orderSeq;
    private String parentID;
    private String samllIcon = "";
    private List<ReleaseGoodsShopListDto> goodsList;

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

    public List<ReleaseGoodsShopListDto> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<ReleaseGoodsShopListDto> goodsList) {
        this.goodsList = goodsList;
    }
}
