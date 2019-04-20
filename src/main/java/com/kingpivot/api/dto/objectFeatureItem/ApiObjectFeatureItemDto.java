package com.kingpivot.api.dto.objectFeatureItem;

public class ApiObjectFeatureItemDto {
    private String objectFeatureItemName;
    private String objectFeatureItemID;
    private  String listImage;//列表图
    private  String faceImage;//押题图

    public String getObjectFeatureItemName() {
        return objectFeatureItemName;
    }

    public void setObjectFeatureItemName(String objectFeatureItemName) {
        this.objectFeatureItemName = objectFeatureItemName;
    }

    public String getObjectFeatureItemID() {
        return objectFeatureItemID;
    }

    public void setObjectFeatureItemID(String objectFeatureItemID) {
        this.objectFeatureItemID = objectFeatureItemID;
    }

    public String getListImage() {
        return listImage;
    }

    public void setListImage(String listImage) {
        this.listImage = listImage;
    }

    public String getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }
}
