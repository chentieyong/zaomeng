package com.kingpivot.api.dto.objectFeatureItem;

import java.util.List;

public class ApiObjectFeatureItemDefineDto {
    private String featureDefineName;
    private String featureDefineID;
    private List<ApiObjectFeatureItemDto> itemList;

    public String getFeatureDefineName() {
        return featureDefineName;
    }

    public void setFeatureDefineName(String featureDefineName) {
        this.featureDefineName = featureDefineName;
    }

    public String getFeatureDefineID() {
        return featureDefineID;
    }

    public void setFeatureDefineID(String featureDefineID) {
        this.featureDefineID = featureDefineID;
    }

    public List<ApiObjectFeatureItemDto> getItemList() {
        return itemList;
    }

    public void setItemList(List<ApiObjectFeatureItemDto> itemList) {
        this.itemList = itemList;
    }
}
