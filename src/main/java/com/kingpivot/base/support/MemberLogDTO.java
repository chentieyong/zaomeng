package com.kingpivot.base.support;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/19.
 */
public class MemberLogDTO implements Serializable {
    private static final long serialVersionUID = -1311306668752973060L;

    private String siteId;

    private String deviceId;

    private String memberId;

    private String appId;

    private String locationId;

    private String objectDefineId;

    private String companyId;

    private String openId;//第三方会员id

    private String openFId;//第三方会员id2

    private String openTrade;//第三方交易密码

    private Boolean realName = false;

    private String memberName;

    private String versionID;

    private String ipAddress;

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public Boolean getRealName() {
        return realName;
    }

    public void setRealName(Boolean realName) {
        this.realName = realName;
    }

    public String getOpenTrade() {
        return openTrade;
    }

    public void setOpenTrade(String openTrade) {
        this.openTrade = openTrade;
    }

    public MemberLogDTO() {
    }

    public MemberLogDTO(String memberId) {
        this.memberId = memberId;
    }

    public MemberLogDTO(String siteId, String appId, String memberId, String companyId) {
        this.siteId = siteId;
        this.appId = appId;
        this.memberId = memberId;
        this.companyId = companyId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getVersionID() {
        return versionID;
    }

    public void setVersionID(String versionID) {
        this.versionID = versionID;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getObjectDefineId() {
        return objectDefineId;
    }

    public void setObjectDefineId(String objectDefineId) {
        this.objectDefineId = objectDefineId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getOpenFId() {
        return openFId;
    }

    public void setOpenFId(String openFId) {
        this.openFId = openFId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
