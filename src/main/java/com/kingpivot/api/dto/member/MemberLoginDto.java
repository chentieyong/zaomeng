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

    private String shortDescription;

    private String description;

    private String weixin;

    private String companyName;

    private String jobName;

    private String loginName;//登录名

    private String phone;//手机号码

    private String weixinToken;//微信unionid

    private String weixinUnionID; //微信唯一ID

    private String recommandID;//推荐人;

    private String recommandMemberName;//推荐人姓名

    private String recommandMemberCode;//推荐会员编号

    private String recommandMemberAvatarURL;//推荐会员头像

    private String recommandCode;//推荐码

    private String recommandChain;//推荐链

    private String siteID;//站点ID

    private String avatarURL;//头像URL

    private String rankID;

    private String rankName;

    private String collectID;

    private String praiseID;

    private String peopleID;

    private String majorName;

    private int isShow = 1;//是否显示 1显示2隐藏

    private String shengID;//省id

    private String shengName;//省id

    private String shiID;//市id

    private String shiName;//市id

    private String xianID;//县id

    private String xianName;//县id

    private String address;//地址

    private String mapX;//经度

    private String mapY;//纬度

    private String titleID;


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

    public String getRankID() {
        return rankID;
    }

    public void setRankID(String rankID) {
        this.rankID = rankID;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public String getRecommandMemberName() {
        return recommandMemberName;
    }

    public void setRecommandMemberName(String recommandMemberName) {
        this.recommandMemberName = recommandMemberName;
    }

    public String getRecommandMemberCode() {
        return recommandMemberCode;
    }

    public void setRecommandMemberCode(String recommandMemberCode) {
        this.recommandMemberCode = recommandMemberCode;
    }

    public String getRecommandMemberAvatarURL() {
        return recommandMemberAvatarURL;
    }

    public void setRecommandMemberAvatarURL(String recommandMemberAvatarURL) {
        this.recommandMemberAvatarURL = recommandMemberAvatarURL;
    }

    public String getCollectID() {
        return collectID;
    }

    public void setCollectID(String collectID) {
        this.collectID = collectID;
    }

    public String getPraiseID() {
        return praiseID;
    }

    public void setPraiseID(String praiseID) {
        this.praiseID = praiseID;
    }

    public String getPeopleID() {
        return peopleID;
    }

    public void setPeopleID(String peopleID) {
        this.peopleID = peopleID;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public String getShengID() {
        return shengID;
    }

    public void setShengID(String shengID) {
        this.shengID = shengID;
    }

    public String getShengName() {
        return shengName;
    }

    public void setShengName(String shengName) {
        this.shengName = shengName;
    }

    public String getShiID() {
        return shiID;
    }

    public void setShiID(String shiID) {
        this.shiID = shiID;
    }

    public String getShiName() {
        return shiName;
    }

    public void setShiName(String shiName) {
        this.shiName = shiName;
    }

    public String getXianID() {
        return xianID;
    }

    public void setXianID(String xianID) {
        this.xianID = xianID;
    }

    public String getXianName() {
        return xianName;
    }

    public void setXianName(String xianName) {
        this.xianName = xianName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMapX() {
        return mapX;
    }

    public void setMapX(String mapX) {
        this.mapX = mapX;
    }

    public String getMapY() {
        return mapY;
    }

    public void setMapY(String mapY) {
        this.mapY = mapY;
    }

    public String getTitleID() {
        return titleID;
    }

    public void setTitleID(String titleID) {
        this.titleID = titleID;
    }
}
