package com.kingpivot.base.memberlog.model;

import com.kingpivot.common.model.BaseModel;

import java.sql.Timestamp;

public class Memberlog extends BaseModel<String> {
    private static final long serialVersionUID = 8881439443364101571L;
    private String id;//主键
    private String memberID;
    private String siteID;
    private Timestamp operateTime;
    private String description;
    private String browserType;
    private String operateType;
    private String locationID;
    private String deviceID;
    private String versionID;//版本ID

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getSiteID() {
        return siteID;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }

    public Timestamp getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Timestamp operateTime) {
        this.operateTime = operateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrowserType() {
        return browserType;
    }

    public void setBrowserType(String browserType) {
        this.browserType = browserType;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getVersionID() {
        return versionID;
    }

    public void setVersionID(String versionID) {
        this.versionID = versionID;
    }

    public enum MemberOperateType {
        MEMBER_LOGIN("会员登录"),GETFOCUSPICTURELIST("轮播列表"),ADDGOODSSHOPTOCART("店铺商品加入购物车"),UPDATECARTGOODSNUMBER("更改购物车商品数量"),
        REMOVECARTGOODS("删除购物车商品"),GETCARTGOODSLIST("获取购物车商品列表"),CREATEMEMBERORDER("店铺商品生成订单"),SELECTCARTGOODS("勾选购物车商品"),
        APPLYMEMBERSHOP("申请会员店铺"),UPDATEMEMBERSHOP("修改会员店铺"),GETMEMBERORDERLIST("获取会员订单列表"),GETMEMBERORDERDETAIL("获取会员订单详情"),
        GETMEMBERSHOPLIST("获取会员店铺列表"),GETMEMBERSHOPDETAIL("获取会员店铺详情"),REMOVEMEMBERSHOP("删除会员店铺"),GETCOLLECTLIST("获取收藏列表"),
        REMOVECOLLECT("删除收藏"),SUBMITONEFEE("提交一个意见反馈"),GETMYMEMBERBONUSLIST("获取我的会员红包列表"),USEMEMBERBONUS("使用会员红包"),
        APPAPPLYMEMBERORDERPAY("APP申请订单支付"),SUBMITONESIGNIN("会员签到"),GETSIGNINLIST("获取签到记录"),GETMYPOINTLIST("获取我的积分列表"),
        GETMYMESSAGELIST("获取我的消息列表"),CANCELMEMBERORDER("取消订单"),CONFIRMMEMBERORDER("确认收货"),APPLYRETURNMEMBERORDERGOODS("申请订单商品退货"),
        GETCANGETBONUSDEFINELIST("获取可领红包定义列表"),MEMBERGETBONUS("会员领红包"),UPDATEMEMBERINFO("修改会员信息"),UPDATELOGINPASSWORD("修改登录密码")
        ,APPLYRETURNMEMBERORDER("申请订单退单"),GETMEMBERRANKINFO("获取会员等级信息"),GETMEMBERINFO("获取会员信息"),READONEMESSAGE("阅读一个消息"),
        GETNOREADMESSAGENUM("获取未阅读消息数量"),GETMEMBERSTATISTICS("获取会员统计信息");
        private String oname;

        MemberOperateType(String oname) {
            this.oname = oname;
        }

        public String getOname() {
            return this.oname;
        }
    }
}