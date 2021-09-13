package com.kingpivot.api.dto.memberWish;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kingpivot.base.member.model.Member;

public class MemberWishListDto {
    private String id;//会员心愿ID
    private String name;
    private String applicationID;//应用ID
    private String memberID;//会员ID
    private String memberName;//会员名称
    private String avatarURL;//头像URL
    @JsonIgnore
    private Member member;
    private String description;//心愿

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

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
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

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
        if (member != null) {
            this.memberName = member.getName();
            this.avatarURL = member.getAvatarURL();
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
