package com.kingpivot.api.dto.blessingTree;

import java.util.List;

public class BlessingTreeListDto {
    private String id;//祈福树ID
    private String name;
    private String description;//描述
    private String backImage;//背景图
    private String faceImage;//押题图
    private List<BlessingTreeDetailListDto> blessingTreeDetailList;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBackImage() {
        return backImage;
    }

    public void setBackImage(String backImage) {
        this.backImage = backImage;
    }

    public String getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }

    public List<BlessingTreeDetailListDto> getBlessingTreeDetailList() {
        return blessingTreeDetailList;
    }

    public void setBlessingTreeDetailList(List<BlessingTreeDetailListDto> blessingTreeDetailList) {
        this.blessingTreeDetailList = blessingTreeDetailList;
    }
}
