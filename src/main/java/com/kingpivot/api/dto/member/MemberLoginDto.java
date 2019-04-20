package com.kingpivot.api.dto.member;

/**
 * Created by Administrator on 2015/10/23.
 */
public class MemberLoginDto {

    private String id;//主键

    private String applicationID;//应用ID

    private String companyID;//公司ID 未实现

    private String name;//名称

    private String shortName;//简称

    private String loginName;//登录名

    private String phone;//手机号码

    private String weixinToken;//微信unionid

    private String weixinUnionID; //微信唯一ID

    private String recommandID;//推荐人;

    private String recommandCode;//推荐码

    private String recommandChain;//推荐链

    private String siteID;//站点ID

    private String avatarURL;//头像URL

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
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

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
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

    public String getWeixinUnionID() {
        return weixinUnionID;
    }

    public void setWeixinUnionID(String weixinUnionID) {
        this.weixinUnionID = weixinUnionID;
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

    public String getSiteID() {
        return siteID;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }
}
