package com.kingpivot.api.dto.member;

import com.kingpivot.common.utils.TimeTest;

import java.sql.Timestamp;

public class MyChildrenMemberList {
    private String id;//主键
    private String name;//名称
    private String shortName;//简称
    private String shortDescription;
    private String description;
    private String loginName;//登录名
    private String loginPassword;//登录密码
    private String weixin;
    private String companyName;
    private String jobName;
    private String phone;//手机号码
    private String weixinToken;//微信unionid
    private String recommandID;//推荐人
    private String recommandCode;//推荐码
    private String recommandChain;//推荐链
    private String avatarURL;//头像URL
    private String rankID;
    private Timestamp createdTime;//创建时间
    private String createdTimeStr;

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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWeixinToken() {
        return weixinToken;
    }

    public void setWeixinToken(String weixinToken) {
        this.weixinToken = weixinToken;
    }

    public String getRecommandID() {
        return recommandID;
    }

    public void setRecommandID(String recommandID) {
        this.recommandID = recommandID;
    }

    public String getRecommandCode() {
        return recommandCode;
    }

    public void setRecommandCode(String recommandCode) {
        this.recommandCode = recommandCode;
    }

    public String getRecommandChain() {
        return recommandChain;
    }

    public void setRecommandChain(String recommandChain) {
        this.recommandChain = recommandChain;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public String getRankID() {
        return rankID;
    }

    public void setRankID(String rankID) {
        this.rankID = rankID;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
        if(createdTime!=null){
            this.createdTimeStr = TimeTest.toDateTimeFormat(createdTime);
        }
    }

    public String getCreatedTimeStr() {
        return createdTimeStr;
    }

    public void setCreatedTimeStr(String createdTimeStr) {
        this.createdTimeStr = createdTimeStr;
    }
}
