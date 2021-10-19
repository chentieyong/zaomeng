package com.kingpivot.api.dto.discuss;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.common.utils.TimeTest;

import java.sql.Timestamp;

public class ObjectDiscussListDto {
    private String id;//主键
    private String name;//主键
    private String description;//主键
    private String objectName;
    private String objectID;
    @JsonIgnore
    private Member member;
    private String memberID;
    private String memberName;
    private String avatarURL;//头像URL
    private Timestamp createdTime;//创建时间
    private String discussDateStr;//评论时间

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

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
        if (member != null) {
            this.memberID = member.getId();
            this.memberName = member.getName();
            this.avatarURL = member.getAvatarURL();
        }
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
        this.discussDateStr = TimeTest.toDateFormat(createdTime);
    }

    public String getDiscussDateStr() {
        return discussDateStr;
    }

    public void setDiscussDateStr(String discussDateStr) {
        this.discussDateStr = discussDateStr;
    }
}
