package com.kingpivot.api.dto.memberRecommend;

import com.kingpivot.common.utils.TimeTest;

import java.sql.Timestamp;

public class MemberRecommendListDto {
    private String name;
    private String avatarURL;
    private Timestamp createdTime;//创建时间
    private String createdTimeStr;//创建时间

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        this.createdTimeStr = TimeTest.toDateTimeFormat(createdTime);
    }

    public String getCreatedTimeStr() {
        return createdTimeStr;
    }

    public void setCreatedTimeStr(String createdTimeStr) {
        this.createdTimeStr = createdTimeStr;
    }
}
