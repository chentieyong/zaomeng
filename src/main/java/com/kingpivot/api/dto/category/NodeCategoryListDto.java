package com.kingpivot.api.dto.category;

import com.kingpivot.api.dto.message.ApiCategoryMessageDto;

import java.util.List;

public class NodeCategoryListDto {
    private String id;
    private String name;
    private String largeIcon;//大图标
    private Long orderSeq;
    private String parentID;
    private String smallIcon;//小图标
    private List<ApiCategoryMessageDto> messageList;

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

    public String getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(String smallIcon) {
        this.smallIcon = smallIcon;
    }

    public List<ApiCategoryMessageDto> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<ApiCategoryMessageDto> messageList) {
        this.messageList = messageList;
    }
}
